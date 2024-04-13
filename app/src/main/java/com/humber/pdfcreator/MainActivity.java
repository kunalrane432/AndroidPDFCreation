package com.humber.pdfcreator;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button createPDFBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createPDFBtn=findViewById(R.id.create_pdf);
        createPDFBtn.setOnClickListener(button_click);


    }

    View.OnClickListener button_click=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                createPDF();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    };

    public void createPDF() throws FileNotFoundException {
        String pdfPath= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();

        File file=new File(pdfPath,"pdfexample.pdf");

        if(file.exists())
             file.delete();
        OutputStream outputStream=new FileOutputStream(file);

        PdfWriter writer=new PdfWriter(file);

        PdfDocument pdfDocument=new PdfDocument(writer);

        Document document=new Document(pdfDocument);

        float columnWidth[]={140,140,140,140};

        Table detailstable=new Table(columnWidth);

        Drawable d1 = getDrawable(R.drawable.invoice);
        Bitmap bitmap1= ((BitmapDrawable)d1).getBitmap();

        ByteArrayOutputStream stream1= new ByteArrayOutputStream();
        bitmap1.compress(Bitmap.CompressFormat.PNG,100,stream1);
        byte[] bitmapData1=stream1.toByteArray();

        ImageData imageData1= ImageDataFactory.create(bitmapData1);

        Image image1=new Image(imageData1);
        image1.setHeight(100f);

        DeviceRgb invoiceGreen=new DeviceRgb(51,204,51);
        DeviceRgb invoiceGray=new DeviceRgb(220,220,220);

        //R1
        detailstable.addCell(new Cell(3,1).add(image1).setBorder(Border.NO_BORDER));
        detailstable.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        detailstable.addCell(new Cell(1,2).add(new Paragraph("Invoice").setFontSize(26f).setFontColor(invoiceGreen)).setBorder(Border.NO_BORDER));


        //R2
        //detailstable.addCell(new Cell().add(new Paragraph("")));
        detailstable.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        detailstable.addCell(new Cell().add(new Paragraph("Invoice No: ")).setBorder(Border.NO_BORDER));
        detailstable.addCell(new Cell().add(new Paragraph("1234567")).setBorder(Border.NO_BORDER));

        //R3
        //detailstable.addCell(new Cell().add(new Paragraph("")));
        detailstable.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        detailstable.addCell(new Cell().add(new Paragraph("Invoice Date: ")).setBorder(Border.NO_BORDER));
        detailstable.addCell(new Cell().add(new Paragraph("04/12/2024")).setBorder(Border.NO_BORDER));


        ArrayList<Product> products=new ArrayList<Product>();
        for (int i=1;i<=10;i++)
        {
            Product product=new Product();
            product.setName("product I : "+i);
            product.setQuantity(i);
            product.setPrice(i*3);
            products.add(product);
        }

        float columnWidthProducts[]={62,162,112,112,112};
        Table productsTable=new Table(columnWidthProducts);
        float totalPrice=0.0f;
        productsTable.addCell(new Cell().add(new Paragraph("Sr No.").setFontColor(ColorConstants.WHITE)).setBackgroundColor(invoiceGreen));
        productsTable.addCell(new Cell().add(new Paragraph("Product Name").setFontColor(ColorConstants.WHITE)).setBackgroundColor(invoiceGreen));
        productsTable.addCell(new Cell().add(new Paragraph("Product Quantity").setFontColor(ColorConstants.WHITE)).setBackgroundColor(invoiceGreen));
        productsTable.addCell(new Cell().add(new Paragraph("Product Price").setFontColor(ColorConstants.WHITE)).setBackgroundColor(invoiceGreen));
        productsTable.addCell(new Cell().add(new Paragraph("Total Price").setFontColor(ColorConstants.WHITE)).setBackgroundColor(invoiceGreen));

        for(int i=0;i<products.size();i++)
        {productsTable.addCell(new Cell().add(new Paragraph(String.valueOf(i))).setBackgroundColor(invoiceGray));
            productsTable.addCell(new Cell().add(new Paragraph(products.get(i).getName())).setBackgroundColor(invoiceGray));
            productsTable.addCell(new Cell().add(new Paragraph(String.valueOf(products.get(i).getQuantity()))).setBackgroundColor(invoiceGray));
            productsTable.addCell(new Cell().add(new Paragraph(String.valueOf(products.get(i).getPrice()))).setBackgroundColor(invoiceGray));
            productsTable.addCell(new Cell().add(new Paragraph(String.valueOf(products.get(i).getQuantity()*products.get(i).getPrice()))).setBackgroundColor(invoiceGray));
            totalPrice=totalPrice+products.get(i).getQuantity()*products.get(i).getPrice();
        }

        productsTable.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        productsTable.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        productsTable.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        productsTable.addCell(new Cell().add(new Paragraph("Sub-total").setFontColor(ColorConstants.WHITE)).setBackgroundColor(invoiceGreen));
        productsTable.addCell(new Cell().add(new Paragraph(String.valueOf(totalPrice)).setFontColor(ColorConstants.WHITE)).setBackgroundColor(invoiceGreen));



        productsTable.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        productsTable.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        productsTable.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        productsTable.addCell(new Cell().add(new Paragraph("Tax(13%)").setFontColor(ColorConstants.WHITE)).setBackgroundColor(invoiceGreen));
        productsTable.addCell(new Cell().add(new Paragraph(String.valueOf(0.13*totalPrice)).setFontColor(ColorConstants.WHITE)).setBackgroundColor(invoiceGreen));

        productsTable.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        productsTable.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        productsTable.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        productsTable.addCell(new Cell().add(new Paragraph("Grand Total").setBold().setFontSize(16).setFontColor(ColorConstants.WHITE)).setBackgroundColor(invoiceGreen));
        productsTable.addCell(new Cell().add(new Paragraph(String.valueOf(totalPrice+(0.13*totalPrice))).setBold().setFontSize(16).setFontColor(ColorConstants.WHITE)).setBackgroundColor(invoiceGreen));
        //pdfDocument.add(table);

        float columnWidthContactUS[]={50,250,260};

        Table contactUsTable=new Table(columnWidthContactUS);

        Drawable d2 = getDrawable(R.drawable.contact_us);
        Bitmap bitmap2= ((BitmapDrawable)d2).getBitmap();

        ByteArrayOutputStream stream2= new ByteArrayOutputStream();
        bitmap2.compress(Bitmap.CompressFormat.PNG,100,stream2);
        byte[] bitmapData2=stream2.toByteArray();

        ImageData imageData2= ImageDataFactory.create(bitmapData2);

        Image image2=new Image(imageData2);
        image2.setHeight(100);


        Drawable d3 = getDrawable(R.drawable.thanks);
        Bitmap bitmap3= ((BitmapDrawable)d3).getBitmap();

        ByteArrayOutputStream stream3= new ByteArrayOutputStream();
        bitmap3.compress(Bitmap.CompressFormat.PNG,100,stream3);
        byte[] bitmapData3=stream3.toByteArray();

        ImageData imageData3= ImageDataFactory.create(bitmapData3);

        Image image3=new Image(imageData3);
        image3.setHeight(100);
        image3.setHorizontalAlignment(HorizontalAlignment.RIGHT);

        contactUsTable.addCell(new Cell(3,1).add(image2).setBorder(Border.NO_BORDER));
        contactUsTable.addCell(new Cell().add(new Paragraph("akshath.dsouza@yedigand.com")).setBorder(Border.NO_BORDER));
        contactUsTable.addCell(new Cell(3,1).add(image3).setBorder(Border.NO_BORDER));
        contactUsTable.addCell(new Cell().add(new Paragraph("123-123-1234")).setBorder(Border.NO_BORDER));
        //contactUsTable.addCell(new Cell().add(new Paragraph("")));
        contactUsTable.addCell(new Cell().add(new Paragraph("Address")).setBorder(Border.NO_BORDER));
        //contactUsTable.addCell(new Cell().add(new Paragraph("")));


        document.add(detailstable);
        document.add(new Paragraph("\n"));
        document.add(productsTable);
        document.add(new Paragraph("\n\n\n\n\n\n(Authorised Signatory)\n\n\n").setTextAlignment(TextAlignment.RIGHT));
        document.add(contactUsTable);
        pdfDocument.close();
        Toast.makeText(this,"pdf created",Toast.LENGTH_LONG).show();
    }

}