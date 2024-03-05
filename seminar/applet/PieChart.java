import java.awt.*;
import java.awt.image.ImageObserver;
import java.applet.*;
import java.net.URL;
import java.net.MalformedURLException;
import java.awt.Frame;

public class PieChart extends Applet 
{
        String fontName;
        Color legendTextColor,pieTextColor,titleTextColor,backgroundColor;
        Image backgroundImage = null;
        Dimension imageDimensions;
        double values[] = new double[100];
        String[] titles = new String[100];
        URL urls[] = new URL[100];
        URL URLoutside = null;
        int valuesSize = 0;
        Font legendFont,titleFont,pieFont;
        int legendFontSize,titleFontSize,pieFontSize;
        Color c[] = new Color[14];
        Rectangle r;
        String chartTitle;
        int topmargin = 10;
        int rightmargin = 10;
        int pieSize = 0,xPos = 0,yPos = 0;
        int mouseSelAngle = -1;
        double sum, factor;
        Graphics g2;
        Image offScreenImage  = null;
        int xMousePos = -1, yMousePos = -1;
        int curPiece = -1;
        boolean wantTips = true;
        Frame parentFrame = null;

        public PieChart()
        {
        }

        
        public String getAppletInfo()
        {
                return "Name: PieChart\r\n" +
                       "Author: Gary T. Desrosiers\r\n" +
                       "Copyright (C) 1996 Gary T. Desrosiers\r\n" +
                           "All rights reserved";
        }

        public String[][] getParameterInfo()
        {
                String[][] info =
                {
                        { "V0 thru Vn", "Double", "values for pie chart" },
                        { "T0 thru Tn", "String", "Titles for each value to display in legend" },
                        { "U0 thru Un", "String", "URL to hyperlink when pie piece is clicked." },
                        { "URLOutside", "String", "URL to hyperlink when user clicks outside pie" },
                        { "ChartTitle","String","Title of the pie chart" },
                        { "TitleTextColor","String","Color of the title" },
                        { "TitleFontSize","Int","Size of chart tile font" },
                        { "LegendTextColor","String","Color of the ledend text" },
                        { "LegendFontSize","Int","Size of legend font" },
                        { "PieTextColor","String","Color of text labeling pie chart" },
                        { "PieFontSize","Int","Size of text labeling pie chart" },
                        { "BackgroundColor","String","Color of the background" },
                        { "Background","String","relative URL to background image" },
                        { "FontName","String","Name of font to use, default is Helvetica" },
                        { "Tips","Boolean","True/False - values displayed as user moves mouse over chart" },

                };
                return info;            
        }

        public boolean imageUpdate(Image img,int infoflags,int x,int y,int width,int height) 
        {
                if((infoflags & ImageObserver.ALLBITS) != 0) {
                        imageDimensions = getImageDimensions(backgroundImage); 
                        repaint();
                        return false;
                }
                return true;

        }
        public void init()
        {
        String param;
        int i;

                c[0] = Color.green;
                c[1] = Color.blue;
                c[2] = Color.orange;
                c[2] = new Color(255,255,128);          // pale yellow
                c[3] = new Color(0,128,0);                      // half green
                c[4] = new Color(128,0,255);            // purple
                c[5] = new Color(0,0,160);                      // dark blue
                c[6]  = Color.red;
                c[7] = new Color(128,192,192);          // steel blue
                c[8] = new Color(64,128,128);           // kackki
                c[9] = new Color(128,255,255);          // aqua
                c[10] = new Color(255,128,192);         // pale pink
                c[11] = Color.white;
                c[12] = Color.darkGray;
                c[13] = new Color(255,128,255);         // pink


                fontName = (param = getParameter("FontName")) == null ? "Helvetica" : param;
                legendFontSize = (param = getParameter("LegendFontSize")) == null ? 14 : Integer.parseInt(param);
                legendFont = new Font(fontName, Font.BOLD, legendFontSize);
                titleFontSize = (param = getParameter("titleFontSize")) == null ? 14 : Integer.parseInt(param);
                titleFont = new Font(fontName, Font.BOLD, titleFontSize);
                pieFontSize = (param = getParameter("PieFontSize")) == null ? 14 : Integer.parseInt(param);
                pieFont = new Font(fontName, Font.BOLD, pieFontSize);

                chartTitle = (param = getParameter("ChartTitle")) == null ? "" : param;

                i = 0;
                while((param = getParameter("V"+i)) != null) 
                {
                        values[i] = Double.valueOf(param).doubleValue();
                        titles[i] = getParameter("T"+i);
                        param = getParameter("U"+i);
                        if(param == null)
                                urls[i] = null;
                        else
                                try
                            {
                                        urls[i] = new URL(param);
                            }
                                catch(MalformedURLException e) { urls[i] = null; }
                        i++;
                }
                valuesSize = i;

                sum = 0;
                for(i=0;i<valuesSize;i++) 
                        sum += values[i];
                factor = 360.0 / sum;

                param = getParameter("URLOutside");
                if(param != null)
                        try {
                                URLoutside = new URL(param);
                    } catch(MalformedURLException e) {
                                URLoutside = null;
                        }

                try 
                {
                        param = getParameter("Background");
                        if (param != null) 
                        {
                                backgroundImage = getImage(new URL(getDocumentBase(),param));
                                imageDimensions = getImageDimensions(backgroundImage); 
                        }
                } catch(MalformedURLException e) 
                {
                        backgroundImage = null;
                }
                
                legendTextColor = (param = getParameter("LegendTextColor")) == null ? Color.red : convertColor(param);
                pieTextColor = (param = getParameter("PieTextColor")) == null ? Color.white : convertColor(param);
                titleTextColor = (param = getParameter("TitleTextColor")) == null ? Color.yellow : convertColor(param);
                backgroundColor = (param = getParameter("BackgroundColor")) == null ? Color.black : convertColor(param);
                
                wantTips = (param = getParameter("Tips")) == null ? true : new Boolean(true).getBoolean(param);

                offScreenImage = createImage(size().width, size().height);
                g2  = offScreenImage.getGraphics();
                r = bounds();

                parentFrame = getParentFrame();
        }

        private Frame getParentFrame()
        {
        Component p=this;
        while(((p=p.getParent())!=null) && !(p instanceof Frame));
        return (Frame)p;
        }

        public void destroy()
        {
        }


        public void paint(Graphics g)
        {
        Rectangle d = bounds();
                if((d.height != r.height) || (d.width != r.width))
                {
                        offScreenImage = createImage(size().width, size().height);
                        g2  = offScreenImage.getGraphics();
                        r = bounds();
                }
                update(g);
        }


        public synchronized void update(Graphics g)
        {
        double anglerr = 0;
        double curVal;
        int startAngle = 0;
        int tipAngle = -1, tip = -1;

                //r = bounds();
                pieSize = r.height-topmargin-rightmargin-(2*g2.getFontMetrics(pieFont).getHeight());
                if (backgroundImage != null) 
                {
                        if((imageDimensions.width != -1) && (imageDimensions.height != -1)) 
                        {
                                int x = 0, y = 0;
                                for(x=0;x<size().width;x+=imageDimensions.width)
                                        for(y=0;y<size().height;y+=imageDimensions.height)
                                                g2.drawImage(backgroundImage, x, y, this);
                        }
                } else
                {
                        g2.setColor(backgroundColor);
                        g2.fillRect(0,0,r.width,r.height);
                }
                startAngle = 0;
                anglerr = 0;
                xPos = r.width-r.height-rightmargin;
                yPos = topmargin+g2.getFontMetrics(pieFont).getHeight();
                int sx = 10;
                int sy = r.height - getFontMetrics(legendFont).getHeight();
                int legendFontHeight = getFontMetrics(legendFont).getHeight();
                for(int i=0;i<valuesSize;i++) 
                {
                        curVal = values[i]*factor;
                        anglerr += (int)curVal;
                        g2.setColor(c[i%c.length]);
                        if(i == (valuesSize-1)) 
                                g2.fillArc(xPos,yPos, pieSize, pieSize, -startAngle,(int)-(curVal+(360.0-anglerr)));
                        else 
                                g2.fillArc(xPos,yPos, pieSize, pieSize, -startAngle,(int)-curVal); 
                
                        tic(g2,Math.round(((values[i]/sum)*10000))/100 + "%",
                                (int)(startAngle+(curVal/2)),
                                xPos+pieSize/2,
                                yPos+pieSize/2,
                                pieSize/2,
                                pieSize/2);
                        g2.setFont(legendFont);
                        g2.setColor(c[i%c.length]);
                        g2.fillOval(sx-legendFontHeight/2,sy-legendFontHeight/2,legendFontHeight/2+1,legendFontHeight/2+1);
                        g2.setColor(legendTextColor);
                        if((mouseSelAngle > startAngle) && (mouseSelAngle < (startAngle+curVal))) {
                                g2.drawRect(sx+legendFontHeight-2,sy-g2.getFontMetrics().getAscent(),g2.getFontMetrics().stringWidth(titles[i])+4,g2.getFontMetrics().getHeight());
                                if(urls[i] != null) {
                                        mouseSelAngle = -1; // deselect in case user backs up to this page
                                        getAppletContext().showDocument(urls[i]);
                                }
                        } 
                        if(piecePos(xMousePos,yMousePos) == i)
                        {
                                tip = i;
                                tipAngle = (int)(startAngle+(curVal/2));
                        }
                        g2.drawString(titles[i], sx+legendFontHeight, sy); 
                        sy -= g2.getFontMetrics().getHeight()-2;
                        startAngle += (int)curVal;
                }

                g2.setFont(titleFont);
                g2.setColor(titleTextColor);
                g2.drawString(chartTitle,10, g.getFontMetrics().getHeight()+2);
                if((wantTips == true) && inCircle(xMousePos,yMousePos) && (tipAngle != -1) && (tip != -1))
                {
                        drawValue(g2,"" + values[tip],
                                tipAngle,
                                xPos+pieSize/2,
                                yPos+pieSize/2,
                                pieSize/2,
                                pieSize/2);
                }
        g.drawImage(offScreenImage, 0, 0, this);
        }

        private void drawValue(Graphics g,String value,int i,int x, int y,int w,int h) {
        double x1,y1;

                x1 = Math.cos((double)i*(Math.PI/180))*(w/3*2);
                y1 = Math.sin((double)i*(Math.PI/180))*(h/3*2);
                g.setFont(pieFont);
                g.setColor(new Color(255,255,128));
                g.fillRect((int)x1+x-(g.getFontMetrics().stringWidth(value)/2)-4,(int)y1+y-g.getFontMetrics().getHeight(),g.getFontMetrics().stringWidth(value)+8,g.getFontMetrics().getHeight()+4);
                g.setColor(Color.black);
                g.drawRect((int)x1+x-(g.getFontMetrics().stringWidth(value)/2)-4,(int)y1+y-g.getFontMetrics().getHeight(),g.getFontMetrics().stringWidth(value)+8,g.getFontMetrics().getHeight()+4);
                g.drawString(commaFormat(value),(int)x1+x-(g.getFontMetrics().stringWidth(value)/2),(int)y1+y);
        }


        private String commaFormat(String s) 
        {
        StringBuffer buf = new StringBuffer();
        int i = s.length()-1;
        int dot = -1;
        int digits = 1;

                if(s.indexOf('e') != -1)
                        return s;

                if((dot = s.indexOf('.')) != -1)
                {       
                        for(;i>=dot;i--)
                                buf.insert(0,s.charAt(i));
                }
                for(;i>=0;i--)
                {
                        buf.insert(0,s.charAt(i));
                        if( ((digits%3) == 0) && (i>0))
                        {
                                buf.insert(0,',');
                        }
                        digits++;
                }
                return buf.toString();
        }

        private int piecePos(int x,int y) 
        {
                double curVal;
                int startAngle = 0;
                int curAngle = xyToAngle(x,y);
                for(int i=0;i<valuesSize;i++) 
                {
                        curVal = values[i]*factor;
                        if((curAngle > startAngle) && (curAngle < (startAngle+curVal))) {
                                return(i);
                        } 
                        startAngle += (int)curVal;
                }
                return -1;
        }
        
        public boolean mouseMove(Event  evt, int  x, int  y)
        {
                xMousePos = x;
                yMousePos = y;
                int piece;
                if(inCircle(x,y))
                {
                        if(parentFrame != null)
                                parentFrame.setCursor(Frame.HAND_CURSOR);
                        if((piece = piecePos(x,y)) != -1) 
                        {
                                if(urls[piece] != null)
                                {
                                        showStatus(urls[piece].toString());
                                } else
                                {
                                        showStatus(titles[piece]);
                                }
                        }
                        if(piece != curPiece)
                        {
                                curPiece = piece;
                                repaint();
                        }

                } else
                {
                        if(parentFrame != null)
                                parentFrame.setCursor(Frame.DEFAULT_CURSOR);
                        if(curPiece != -1) 
                        {
                                curPiece = -1;
                                repaint();
                        }
                        showStatus("Select a pie piece");
                }
                return true;
        }

        public boolean mouseExit(Event evt, int x,int y) 
        {
                showStatus("");
                xMousePos = -1;
                yMousePos = -1;
                return true;
        }

        public boolean mouseUp(Event  evt, int  x, int  y)
        {
                if(inCircle(x,y)) {
                        mouseSelAngle = xyToAngle(x,y);
                } else {
                                mouseSelAngle = -1; 
                                if(URLoutside != null) 
                                        getAppletContext().showDocument(URLoutside);
                }
                repaint();
                return true;
        }


        private int xyToAngle(int x,int y) 
        {
                double radius = pieSize/2;
                double t = Math.atan2(y-(yPos+radius),x-(xPos+radius))/(Math.PI/180);
                if(t < 0) 
                {
                        t = 180+(180-Math.abs(t));
                }
                return new Double(t).intValue();
        }



        private boolean inCircle(int xIn,int yIn)
        {
    double a = pieSize / 2;
    double b = pieSize / 2;
    double x = xIn - xPos - a;
    double y = yIn - yPos - b;
    return (((x * x) / (a * a) + (y * y) / (b * b)) <= 1);
        }

        
        private void tic(Graphics g,String ticmsg,int i,int x,int y, int w,int h) {
        double x1,y1,x2,y2;

                x1 = Math.cos((double)i*(Math.PI/180))*w;
                y1 = Math.sin((double)i*(Math.PI/180))*h;
                x2 = Math.cos((double)i*(Math.PI/180))*(w+10);
                y2 = Math.sin((double)i*(Math.PI/180))*(h+10);
                g.setColor(pieTextColor);
                g.setFont(pieFont);
                g.drawLine((int)x1+x,(int)y1+y,(int)x2+x,(int)y2+y);
                if(x2 > x1)
                        g.drawString(ticmsg,(int)x2+x,(int)(y2>y1 ? g.getFontMetrics().getHeight() +y2+y : y2+y));
                else
                        g.drawString(ticmsg,(int)x2+x-g.getFontMetrics().stringWidth(ticmsg),(int)(y2>y1 ? g.getFontMetrics().getHeight() +y2+y : y2+y));
        }


    Dimension getImageDimensions(Image im)
        {
                return new Dimension(im.getWidth(this),im.getHeight(this));
    }

        private Color convertColor(String c) 
        {
                if(c.equalsIgnoreCase("black"))
                        return Color.black;
                if(c.equalsIgnoreCase("blue"))
                        return Color.blue;
                if(c.equalsIgnoreCase("cyan"))
                        return Color.cyan;
                if(c.equalsIgnoreCase("darkGray"))
                        return Color.darkGray;
                if(c.equalsIgnoreCase("gray"))
                        return Color.gray;
                if(c.equalsIgnoreCase("green"))
                        return Color.green;
                if(c.equalsIgnoreCase("lightGray"))
                        return Color.lightGray;
                if(c.equalsIgnoreCase("magenta"))
                        return Color.magenta;
                if(c.equalsIgnoreCase("orange"))
                        return Color.orange;
                if(c.equalsIgnoreCase("pink"))
                        return Color.pink;
                if(c.equalsIgnoreCase("red"))
                        return Color.red;
                if(c.equalsIgnoreCase("white"))
                        return Color.white;
                if(c.equalsIgnoreCase("yellow"))
                        return Color.yellow;
                return Color.black;
        }
}