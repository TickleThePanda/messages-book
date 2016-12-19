package uk.co.ticklethepanda.messagesbook.pdfwriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Calendar;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.FontSelector;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import uk.co.ticklethepanda.messagesbook.ConfigLoader;
import uk.co.ticklethepanda.messagesbook.messagesdatastructure.DateAndTime;
import uk.co.ticklethepanda.messagesbook.messagesdatastructure.MessageData;
import uk.co.ticklethepanda.messagesbook.messagesdatastructure.MessageList;
import uk.co.ticklethepanda.messagesbook.messagesdatastructure.MessageType;
import uk.co.ticklethepanda.messagesbook.messagesdatastructure.Person;

public class CreateDocument {

    private static final float CM_IN_POINTS = 28.35f;

    private static final int PAGE_HEIGHT = 720;
    private static final int PAGE_WIDTH = 576;
    private static final float EXTRA_BORDER = 1.5f * CM_IN_POINTS;
    private static final float INNER_BORDER = EXTRA_BORDER;
    private static final float OUTER_BORDER = EXTRA_BORDER;
    private static final float INDENTATION_SIZE =
            (PAGE_WIDTH - (INNER_BORDER + OUTER_BORDER))
                    / (float) (Math.pow(Math.sqrt(2), 6));

    // FONT SIZES
    private static final float FONT_SIZE = 9f;
    private static final float TITLE_FONT_SIZE = 32.0f;
    private static final float SUBTITLE_FONT_SIZE = 18.0f;

    private static final float RELATIVE_LEADING_SIZE = 1f;
    private static final float RELATIVE_BODY_OFFSET = 1.25f;

    private static final String FONT_DIR = ConfigLoader.getProperties().getProperty("font.location");

    private static final String TIMES_BOLD_LOC = FONT_DIR + "timesbd.ttf";
    private static final String TIMES_ITALIC_LOC = FONT_DIR + "timesi.ttf";
    private static final String TIMES_LOC = FONT_DIR + "times.ttf";

    private static final String OUTPUT = ConfigLoader.getProperties().getProperty("output.location");
    private static final String TEMP_OUT = OUTPUT + "temp/";
    private static final String OUT_NAME = "output";

    private static final String MAIN_TITLE = "What We Wrote: Volume ";
    private static final String SUB_TITLE = "By Jess and Panda";

    private static final int MAX_NUMBER_MESSAGE_PAGES = 399;
    private static final int MAX_PAGES = 400;

    private static final Calendar lastDate = Calendar.getInstance();

    public static void
            createDocumentsFromMessageList(final MessageList messages) throws Throwable{

        lastDate.setTimeInMillis(1314285660000L);

        int count = 1;
        CreateDocument cd = null;
        cd = new CreateDocument(count);
        for (int i = 0; i < messages.length(); i++) {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(messages.getMessage(i).getDateAndTime()
                    .getTimestamp());

            if (c.after(lastDate)) {
                if (cd.getPw().getCurrentPageNumber() >= MAX_NUMBER_MESSAGE_PAGES) {
                    i--;
                    cd.finish();
                    cd.finalize();
                    count++;
                    System.out.println("\t Output " + count + " finished");
                    cd = new CreateDocument(count);
                }
                cd.addMessage(messages.getMessage(i));
            }
        }
        cd.finish();
        cd.finalize();

        // get output files
        File dir = new File(TEMP_OUT);
        File[] files = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(final File dir, final String name) {
                return name.endsWith(".pdf");
            }
        });

        // for each of them remove that stupid fucking page
        for (File file : files) {
            PdfReader pr = new PdfReader(file.getPath());
            if (pr.getNumberOfPages() == 401) {
                String pages =
                        "1-" + String.valueOf(pr.getNumberOfPages() - 3) + ","
                                + String.valueOf(pr.getNumberOfPages() - 1)
                                + "-401";
                pr.selectPages(pages);
            }
            PdfStamper ps =
                    new PdfStamper(pr, new FileOutputStream(OUTPUT
                            + file.getName()));
            ps.close();
        }

    }

    private final Document document;
    private FontSelector titleText;
    private FontSelector bodyText;
    private FontSelector headerText;
    private FontSelector dateText;
    private FontSelector subTitleText;

    private Paragraph currentBody = new Paragraph();

    private Person lastPerson = Person.NONE;

    private long lastTime = 1L;
    private final PdfWriter pw;

    public PdfWriter getPw() {
        return pw;
    }

    private MessageType lastMessageType = MessageType.NONE;
    private int number;

    public CreateDocument(final int number) throws DocumentException,
            IOException {

        FileOutputStream fos = null;
        fos =
                new FileOutputStream(TEMP_OUT + OUT_NAME
                        + String.valueOf(number) + ".pdf");
        FontFactory.defaultEmbedding = true;

        this.number = number;

        this.document = new Document();

        this.pw = PdfWriter.getInstance(this.document, fos);
        // this.pw.setPDFXConformance(PdfWriter.PDFX32002);

        createFonts();

        this.document.setPageSize(new Rectangle(PAGE_WIDTH, PAGE_HEIGHT));

        this.document.open();

        createFirstPages();

        this.document.setMargins(INNER_BORDER, OUTER_BORDER, OUTER_BORDER,
                OUTER_BORDER);
        this.document.setMarginMirroring(true);

    }

    private void addBody(MessageData message) {
        Paragraph p =
                new Paragraph(getPhrase(message.getMessage(), this.bodyText));
        p.setLeading(0, RELATIVE_BODY_OFFSET);

        if (message.getPerson() == Person.JESS) {
            p.setIndentationLeft(INDENTATION_SIZE);
        } else if (message.getPerson() == Person.PANDA) {
            p.setIndentationRight(INDENTATION_SIZE);
        }

        this.currentBody.add(p);
    }

    private void addDate(MessageData md) throws DocumentException {
        Paragraph p =
                new Paragraph(getPhrase(md.getDateAndTime().getDateAsString(),
                        this.dateText));

        this.document.add(p);

    }

    private void addHeader(final MessageData message) throws DocumentException {
        this.currentBody.setKeepTogether(true);
        this.currentBody.setLeading(0, RELATIVE_LEADING_SIZE);

        Paragraph nameParagraph = null;

        if (message.getPerson() == Person.JESS) {
            nameParagraph = new Paragraph(getPhrase("Jess", this.headerText));
            this.currentBody.setIndentationLeft(INDENTATION_SIZE);
            nameParagraph.setIndentationLeft(INDENTATION_SIZE);
        } else if (message.getPerson() == Person.PANDA) {
            nameParagraph = new Paragraph(getPhrase("Panda", this.headerText));
            nameParagraph.setIndentationRight(INDENTATION_SIZE);
            this.currentBody.setIndentationRight(INDENTATION_SIZE);
        }
        this.currentBody.add(nameParagraph);

        final Paragraph paragraph =
                new Paragraph(getPhrase(message.getMessageTypeAsString()
                        + message.getDateAndTime().getTimeAsString(),
                        this.headerText));
        if (message.getPerson() == Person.JESS) {
            paragraph.setIndentationLeft(INDENTATION_SIZE);
        } else if (message.getPerson() == Person.PANDA) {
            paragraph.setIndentationRight(INDENTATION_SIZE);
        }

        paragraph.setAlignment(Element.ALIGN_RIGHT);
        paragraph.setLeading(0, 0f);
        paragraph.setSpacingAfter(-FONT_SIZE);

        this.currentBody.add(paragraph);

        final Paragraph p2 = getLineSeparatorAsParagraph();
        if (message.getPerson() == Person.JESS) {
            p2.setIndentationLeft(INDENTATION_SIZE);
        } else if (message.getPerson() == Person.PANDA) {
            p2.setIndentationRight(INDENTATION_SIZE);
        }

        p2.setSpacingAfter(-FONT_SIZE / 4);

        this.currentBody.add(p2);

    }

    public final void addMessage(final MessageData message)
            throws DocumentException {
        if (DateAndTime.dayIsAfterLast(message.getDateAndTime().getTimestamp(),
                this.lastTime)) {
            this.document.add(this.currentBody);
            this.document.newPage();
            addDate(message);
            this.currentBody = new Paragraph();

            addHeader(message);
        } else if (lastPerson != message.getPerson()
                || lastMessageType != message.getMessageType()) {
            this.document.add(this.currentBody);

            this.currentBody = new Paragraph();

            addHeader(message);
        }
        this.lastTime = message.getDateAndTime().getTimestamp();
        this.lastPerson = message.getPerson();
        this.lastMessageType = message.getMessageType();

        addBody(message);
    }

    private void addNewPage() throws DocumentException {
        this.document.newPage();
        pw.setPageEmpty(false);
    }

    private void createFirstPages() throws DocumentException {

        // First page
        addNewPage();
        // Second page
        addNewPage();

        // Title page
        addNewPage();
        Paragraph title =
                new Paragraph(
                        getPhrase(MAIN_TITLE + String.valueOf(number + 1),
                                this.titleText));
        title.setAlignment(Element.ALIGN_CENTER);
        title.setLeading(150);
        this.document.add(title);

        Paragraph subTitle =
                new Paragraph(getPhrase(SUB_TITLE, this.subTitleText));
        subTitle.setAlignment(Element.ALIGN_CENTER);
        subTitle.setLeading(TITLE_FONT_SIZE);
        this.document.add(subTitle);

        addNewPage();
    }

    private FontSelector createFont(String fontLocation, float fontSize)
            throws DocumentException, IOException {
        FontSelector tempFs = new FontSelector();
        BaseFont tempBf =
                BaseFont.createFont(fontLocation, BaseFont.IDENTITY_H,
                        BaseFont.EMBEDDED);
        Font tempFont = new Font(tempBf, fontSize, Font.NORMAL);
        tempFs.addFont(tempFont);
        return tempFs;
    }

    private void createFonts() throws IOException, DocumentException {
        bodyText = createFont(TIMES_LOC, FONT_SIZE);
        dateText = createFont(TIMES_BOLD_LOC, FONT_SIZE);
        headerText = createFont(TIMES_ITALIC_LOC, FONT_SIZE);
        titleText = createFont(TIMES_LOC, TITLE_FONT_SIZE);
        subTitleText = createFont(TIMES_LOC, SUBTITLE_FONT_SIZE);
    }

    public final void finish() throws DocumentException {
        while (pw.getCurrentPageNumber() <= MAX_PAGES) {
            addNewPage();
        }
        this.document.close();
    }

    private Paragraph getLineSeparatorAsParagraph() {
        Paragraph line = new Paragraph();
        final LineSeparator ls = new LineSeparator();
        ls.setLineWidth(0.4f);
        ls.setLineColor(BaseColor.BLACK);
        line.add(ls);
        return line;
    }

    private Phrase getPhrase(final String str, final FontSelector fs) {
        return fs.process(str);
    }

}