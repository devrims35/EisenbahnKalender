package de.beispiel.eisenbahnkalender;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {
    private final String[] headings = {"Wochentag", "Tag", "Monat", "Wetter", "Jahreszeit"};
    private String[] values = new String[5];
    private final int[] colors = {0xfff5c146,0xff74c5e8,0xffa7d47e,0xffee9ac5,0xffaf9add};
    private SharedPreferences prefs;
    private TrainView train;
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        prefs = getSharedPreferences("calendar", MODE_PRIVATE);
        initializeValues();
        train = new TrainView();
        setContentView(train);
    }
    private void initializeValues() {
        Date now = new Date(); Locale de = Locale.GERMAN;
        String[] defaults = {
            new SimpleDateFormat("EEEE", de).format(now),
            new SimpleDateFormat("d", de).format(now),
            new SimpleDateFormat("MMMM", de).format(now),
            "Sonnig", season(now)
        };
        for(int i=0;i<5;i++) values[i] = prefs.getString("field"+i, defaults[i]);
    }
    private String season(Date now) {
        int month = Integer.parseInt(new SimpleDateFormat("M", Locale.GERMAN).format(now));
        if(month==12||month<=2) return "Winter";
        if(month<=5) return "Frühling";
        if(month<=8) return "Sommer";
        return "Herbst";
    }
    private void editField(int index) {
        EditText entry = new EditText(this);
        entry.setText(values[index]); entry.setSingleLine(true);
        LinearLayout layout = new LinearLayout(this);
        layout.setPadding(48,16,48,8); layout.addView(entry, new LinearLayout.LayoutParams(-1,-2));
        new AlertDialog.Builder(this).setTitle(headings[index]+" einstellen")
            .setView(layout).setPositiveButton("Speichern",(d,w)->{
                values[index] = entry.getText().toString().trim();
                prefs.edit().putString("field"+index, values[index]).apply(); train.invalidate();
            }).setNegativeButton("Abbrechen",null).show();
    }
    private class TrainView extends View {
        Paint p = new Paint(3);
        TrainView() { super(MainActivity.this); }
        void rect(Canvas c, float l,float t,float r,float b,float radius,int color) {
            p.setColor(color);p.setStyle(Paint.Style.FILL);c.drawRoundRect(new RectF(l,t,r,b),radius,radius,p);
        }
        void line(Canvas c,float x1,float y1,float x2,float y2,int col,float width) {
            p.setColor(col);p.setStrokeWidth(width);p.setStrokeCap(Paint.Cap.ROUND);c.drawLine(x1,y1,x2,y2,p);
        }
        void text(Canvas c,String s,float x,float y,float size,int color,boolean bold) {
            p.setColor(color);p.setTypeface(bold?android.graphics.Typeface.create("sans-serif",android.graphics.Typeface.BOLD):android.graphics.Typeface.create("sans-serif",android.graphics.Typeface.NORMAL));
            p.setTextSize(size);p.setTextAlign(Paint.Align.CENTER);p.setStyle(Paint.Style.FILL);c.drawText(s,x,y,p);
        }
        void wheel(Canvas c,float x,float y) {
            p.setColor(0xff333945);c.drawCircle(x,y,17,p);p.setColor(0xffe9eeee);c.drawCircle(x,y,8,p);
        }
        @Override protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            float width=getWidth(),height=getHeight();
            float sx=width/1200f, sy=height/550f;
            canvas.save(); canvas.scale(sx,sy);
            canvas.drawColor(0xffedf8ff);
            rect(canvas,0,380,1200,550,0,0xffa2d77d);
            for(int y=435;y<=457;y+=13) line(canvas,20,y,1180,y,0xff58616d,7);
            text(canvas,"MEIN EISENBAHN-KALENDER",600,65,38,0xff275275,true);
            text(canvas,"Tippe auf einen Waggon zum Einstellen",600,99,19,0xff58778b,false);
            // Locomotive
            rect(canvas,28,264,175,402,17,0xffdf4145);
            rect(canvas,55,240,126,302,8,0xffdf4145);
            rect(canvas,69,254,112,291,4,0xffd8eff7);
            rect(canvas,128,305,213,397,9,0xffdf4145);
            rect(canvas,144,281,187,317,4,0xffdf4145);
            rect(canvas,151,254,180,288,4,0xff3c4650);
            rect(canvas,22,384,214,404,4,0xff952d36);
            wheel(canvas,63,415);wheel(canvas,146,415);wheel(canvas,184,415);
            p.setColor(0xfff6d66a);canvas.drawCircle(206,342,10,p);
            text(canvas,"LOS!",104,367,24,Color.WHITE,true);
            for(int i=0;i<5;i++) {
                float x=232+i*189;
                line(canvas,x-15,395,x,395,0xff515a63,7);
                rect(canvas,x,229,x+175,400,17,colors[i]);
                rect(canvas,x+11,241,x+164,282,10,0x66ffffff);
                text(canvas,headings[i],x+87,269,19,0xff273c4b,true);
                rect(canvas,x+11,291,x+164,373,10,0xfffafbf8);
                String value=values[i];
                float font=value.length()>12?17:value.length()>8?21:27;
                text(canvas,value,x+87,340,font,0xff284b61,true);
                rect(canvas,x-3,383,x+178,404,6,0xff515d68);
                wheel(canvas,x+40,415);wheel(canvas,x+136,415);
            }
            canvas.restore();
        }
        @Override public boolean onTouchEvent(android.view.MotionEvent event) {
            if(event.getAction()!=android.view.MotionEvent.ACTION_UP)return true;
            float x=event.getX()*1200f/getWidth(),y=event.getY()*550f/getHeight();
            if(y>=225&&y<=410)for(int i=0;i<5;i++)if(x>=232+i*189&&x<=407+i*189){editField(i);break;}
            return true;
        }
    }
}
