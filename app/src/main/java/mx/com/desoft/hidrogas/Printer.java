package mx.com.desoft.hidrogas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.pdf.PrintedPdfDocument;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by sdavies on 17/01/2014.
 */
public class Printer extends PrintDocumentAdapter {

    private String ticket;
    private int pageCount;
    private Context context;
    private PrintedPdfDocument pdfDocument;

    public Printer(String ticket, Context cxt) {
        this.ticket = ticket;
        this.context = cxt;
    }

    @Override
    public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, final LayoutResultCallback callback, Bundle extras) {

        // Register a cancellation listener
        cancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener() {
            @Override
            public void onCancel() {
                callback.onLayoutCancelled();
            }
        });

        // Prepare the layout.
        int newPageCount;
        // Mils is 1/1000th of an inch. Obviously.
        if(newAttributes.getMediaSize().getHeightMils() < 10000) {
            newPageCount = 2;
        } else {
            newPageCount = 1;
        }

        // Create the PDF document we'll use later
        pdfDocument = new PrintedPdfDocument(context, newAttributes);


        // Has the layout actually changed?
        boolean layoutChanged = newPageCount != pageCount;
        pageCount = newPageCount;

        // Create the doc info to return
        PrintDocumentInfo info = new PrintDocumentInfo
                .Builder("print_output.pdf")
                .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                .setPageCount(pageCount)
                .build();

        // Not actually going to do anything for now
        callback.onLayoutFinished(info, layoutChanged);
    }

    @Override
    public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, final WriteResultCallback callback) {

        // Register a cancellation listener
        cancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener() {
            @Override
            public void onCancel() {
                // If cancelled then ensure that the PDF doc gets thrown away
                pdfDocument.close();
                pdfDocument = null;
                // And callback
                callback.onWriteCancelled();
            }
        });

        // Iterate through the pages
        for (int currentPageNumber = 0; currentPageNumber < pageCount; currentPageNumber++) {
            // Has this page been requested?
            if(!pageRangesContainPage(currentPageNumber, pages)) {
                // Skip this page
                continue;
            }

            // Start the current page
            PdfDocument.Page page = pdfDocument.startPage(currentPageNumber);

            // Get the canvas for this page
            Canvas canvas = page.getCanvas();

            // Draw on the page
            drawPage(currentPageNumber, canvas);

            // Finish the page
            pdfDocument.finishPage(page);
        }

        // Attempt to send the completed doc out
        try {
            pdfDocument.writeTo(new FileOutputStream(destination.getFileDescriptor()));
        } catch (IOException e) {
            callback.onWriteFailed(e.toString());
            return;
        } finally {
            pdfDocument.close();
            pdfDocument = null;
        }

        // The print is complete
        callback.onWriteFinished(pages);
    }


    private boolean pageRangesContainPage(int pageNumber, PageRange[] ranges)
    {
        for(PageRange range : ranges) {
            if(pageNumber >= range.getStart() && pageNumber <= range.getEnd()) {
                return true;
            }
        }
        return false;
    }

    private void drawPage(int pageNumber, Canvas canvas) {
            // Same rect for image and text
        Rect contentRect = new Rect(10, 10, canvas.getWidth() - 10, canvas.getHeight() - 10);
            // Image on page 0, text on page 1
        drawText(ticket, canvas, contentRect);

    }

    private void drawText(String text, Canvas canvas, Rect rect) {

        TextPaint paint = new TextPaint();
        paint.setColor(Color.BLACK);

        StaticLayout sl = new StaticLayout(text, paint, (int)rect.width(), Layout.Alignment.ALIGN_CENTER, 1, 1, false);

        canvas.save();
        canvas.translate(rect.left, rect.top);
        sl.draw(canvas);
        canvas.restore();

    }

}
