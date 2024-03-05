import java.awt.*;
import java.lang.Math;
import java.util.*;
import TextPanel;

//
// ChartPanelArc
//

public class ChartPanelArc extends Panel
{
    // 그래프 색상 배열
    Color GraphColor[] = {
      new Color(0, 135,  64),
      new Color(221, 127,  164),
      new Color(0,  99, 165),
      new Color(220,  96, 0),
      new Color(146, 192,  0),
      new Color(57,  24, 123),
      new Color(229,  50, 36),
      new Color(255, 236, 103),
      new Color(157,  124, 223),
      new Color(129,  150, 136),
      new Color(255, 236, 103),
      new Color(221,  250, 36),
      new Color(25, 36, 131),
      new Color(74,  01, 07),
      new Color(173,  03, 26),
      new Color(70, 03, 27)
    };

    String RowTitle, RowItems[];
    String ColTitle, ColItems[];
    float Data[][]; 
    static float dd[][];
    
    Choice lstRowItems;
    Canvas_ArcChart Graph;
    TextPanel tpColItems;
    
    public ChartPanelArc( String GraphTitle,
                          String RowTitle, String RowItems[],
                          String ColTitle, String ColItems[],
                          float Data[][] )
    {
       this.RowTitle = RowTitle; 
       this.RowItems = RowItems;
       this.ColTitle = ColTitle; 
       this.ColItems = ColItems;
       this.Data = Data; 
        
        setLayout( new BorderLayout() );

        dd = new float[ RowItems.length ][ ColItems.length ];
        for( int i = 0; i < RowItems.length; i ++ ){
          for( int j = 0; j < ColItems.length; j ++ )
            dd[i][j] = Data[i][j];
          ConvertPercent( dd[i] );
        }

	    // 그래프 캔버스를 생성한다.
        Graph = new Canvas_ArcChart(
                      GraphTitle,
                      RowTitle, RowItems,
                      ColTitle, ColItems,
                      dd );

        Graph.SetCurIndex( 0 );
        
        // 리스트 박스를 생성한다.
        lstRowItems = new Choice();
        for( int i = 0; i < RowItems.length; i ++ ){
          lstRowItems.addItem( RowItems[i] );
        }
        
        add("South", lstRowItems );
        add("Center", Graph );
        
        // 텍스트 패널을 생성한다.
        tpColItems = new TextPanel( this, ColItems.length );
        tpColItems.setBackColor(Color.lightGray);
        tpColItems.SetTitle( ColTitle + "(" + RowItems[0] + ")");
        
        for( int i = 0; i < ColItems.length; i ++ ){
            tpColItems.Add( GraphColor[i], ColItems[i] + " : " +  Data[0][i]
                            + "(" + dd[0][i] + "%)");
        }
        
        add(tpColItems );
        setBackground(Color.white);
    }

    // Image 포함 생성자
    
    public ChartPanelArc( String GraphTitle,
                          String RowTitle, String RowItems[],
                          String ColTitle, String ColItems[],
                          float Data[][], Image img )
    {
        this.RowTitle = RowTitle; 
        this.RowItems = RowItems;
        this.ColTitle = ColTitle; 
        this.ColItems = ColItems;
        this.Data = Data; 
        
        setLayout( new BorderLayout() );

        dd = new float[ RowItems.length ][ ColItems.length ];
        for( int i = 0; i < RowItems.length; i ++ ){
          for( int j = 0; j < ColItems.length; j ++ )
            dd[i][j] = Data[i][j];
          ConvertPercent( dd[i] );
        }
               

        // 그래프 캔버스를 생성한다.
        Graph = new Canvas_ArcChart(
                      GraphTitle,
                      RowTitle, RowItems,
                      ColTitle, ColItems,
                      dd, img );

        Graph.SetCurIndex( 0 );
        
        // 텍스트 패널을 생성한다.
        tpColItems = new TextPanel( this, ColItems.length );
        tpColItems.setBackColor(Color.lightGray);
        tpColItems.SetTitle( ColTitle + "(" + RowItems[0] + ")");
        
        for( int i = 0; i < ColItems.length; i ++ ){
            tpColItems.Add( GraphColor[i], ColItems[i] + " : " +  Data[0][i]
                            + "(" + dd[0][i] + "%)");
        }
        
        add(tpColItems );
       
        // 리스트 박스를 생성한다.
        lstRowItems = new Choice();
        for( int i = 0; i < RowItems.length; i ++ ){
          lstRowItems.addItem( RowItems[i] );
        }

        add("South", lstRowItems );
        add("Center", Graph );
                        
        setBackground(Color.white);
        
    }

    public void ConvertPercent( float Data[] )
    {
        int ll = Data.length;
        
        float sum = 0;
        for( int i = 0; i < ll; i ++ ) sum += Data[i];

        if( sum == 0 ){
          for( int i = 0; i < ll; i ++ ) Data[i] = 0;
        } else {
          for( int i = 0; i < ll; i ++ ) Data[i] = (float)((int)(Data[i] / sum * 10000) / 100.);
        }
    }

    public void paint(Graphics g)
    {
    }

    public boolean action( Event e, Object what )
    {
        //if( what == lstRowItems ){
        if( e.target == lstRowItems ){
          Graph.SetCurIndex( lstRowItems.getSelectedIndex() );
          Graph.repaint();
          
          //text panel change
          int ti = lstRowItems.getSelectedIndex();
          
          //Debuging code
          System.out.println("----->" + ti);
          
          tpColItems.Clear();
          tpColItems.SetTitle( ColTitle + "(" + RowItems[ti] + ")");
          
          try{
            for( int i = 0; i < ColItems.length; i ++ ){
              tpColItems.Add( GraphColor[i], ColItems[i] + " : " +  Data[ti][i]
                            + "(" + dd[ti][i] + "%)");
            }
          } catch (NullPointerException err){
            System.out.println(err);     
          }  
          
          tpColItems.repaint();
           
        }

        return( true );
    }
}


//
// 챠트 캔버스 클래스
//

class Canvas_ArcChart extends Canvas
{
    // 그래프 색상 배열
    Color GraphColor[] = {
      new Color(0, 135,  64),
      new Color(221, 127,  164),
      new Color(0,  99, 165),
      new Color(220,  96, 0),
      new Color(146, 192,  0),
      new Color(57,  24, 123),
      new Color(229,  50, 36),
      new Color(255, 236, 103),
      new Color(157,  124, 223),
      new Color(129,  150, 136),
      new Color(255, 236, 103),
      new Color(221,  250, 36),
      new Color(25, 36, 131),
      new Color(74,  01, 07),
      new Color(173,  03, 26),
      new Color(70, 03, 27)
    };

    // 좌표축 크기 설정
    final static int BaseX = 50, BaseY = 50;
    final static int CIRCLE_W = 200, CIRCLE_H = 200;
    final static int SHADOW_D = 40;

    int CircleH = 200, ShadowD = 0;
    int ViewAngle = 30;
    int XGrid = 400, YGrid = 50;

    // 좌표단위 설정(동적) 
    static int ParameterNumber = 11;
    int GridInsetX, GridInsetY ;
    int DataStep = 10;
    static long STEP = 1;
    static float MaxGrid;
    String TitleName, ValueTitle;
   
    // 파라미터로 넘겨받은 데이터 값
    String GraphTitle;
    String RowTitle, RowItems[];
    String ColTitle, ColItems[];
    float Data[][];
   
    // 기본 인덱스 값
    int curIndex;

    // 표시할 레이블
    int OldMouseX, OldMouseY;
    String OldToolTip;
    int MouseX, MouseY;
    String ToolTip;
    
    Image img;
   
    public Canvas_ArcChart( String GraphTitleName,
                            String RowTitle, String RowItems[],
                            String ColTilte, String ColItems[],
                            float Data[][] )
    {                      
        // 입력받은 값을 저장한다
        this.GraphTitle = GraphTitle;
        this.RowTitle = RowTitle;
        this.RowItems = RowItems;
        this.ColTitle = ColTitle;
        this.ColItems = ColItems;
        this.Data     = Data;

        //setFont( new Font( "SYSTEM", Font.BOLD, 12 ) );
              
        setBackground( Color.white );
        SetViewAngle( 40 );
        SetCurIndex( 0 );
    }
    
    public Canvas_ArcChart( String GraphTitleName,
                            String RowTitle, String RowItems[],
                            String ColTilte, String ColItems[],
                            float Data[][], Image img )
    {                      
        // 입력받은 값을 저장한다
        this.GraphTitle = GraphTitle;
        this.RowTitle = RowTitle;
        this.RowItems = RowItems;
        this.ColTitle = ColTitle;
        this.ColItems = ColItems;
        this.Data     = Data;
        this.img      = img;

        //setFont( new Font( "SYSTEM", Font.BOLD, 12 ) );
              
        setBackground( Color.white );
        SetViewAngle( 65 );
        SetCurIndex( 0 );
    }
   
    public void SetViewAngle( int angle )
    {
        double rd, rd2;
      
        ViewAngle = angle;
        rd  = (double)angle / 180 * java.lang.Math.PI;
        rd2 = (double)(90-angle) / 180 * java.lang.Math.PI; 
      
        CircleH = (int)(java.lang.Math.sin( rd ) * CIRCLE_H);
        ShadowD = (int)(java.lang.Math.sin( rd2 ) * SHADOW_D );
    }

    public void putXYData(Graphics g)
    {
        int count;
        double CurAngle, DataAngle;

        //g.setColor( new Color(32, 32, 32) );
        //g.fillOval( BaseX, BaseY+ShadowD, CIRCLE_W, CircleH );
        
        // 배경
        CurAngle = 90;
        count = ColItems.length;
        for( int i = 0; i < count; i ++ ){
          DataAngle = Data[curIndex][i] * -3.6;
        
 	      g.setColor( GraphColor[i].darker() );
	      g.fillArc( BaseX, BaseY + ShadowD, CIRCLE_W, CircleH,
		             (int)CurAngle, (int)DataAngle - 1 );
	      CurAngle += DataAngle;
        }
        
        // data chart CIRCLE_W = 200, CIRCLE_H = 200
        for( int i = 0; i < count; i ++ ){
          DataAngle = Data[curIndex][i] * -3.6;
        
 	      g.setColor( GraphColor[i] );
	      g.fillArc( BaseX, BaseY, CIRCLE_W, CircleH,
		             (int)CurAngle, (int)DataAngle - 1 );
	      drawPercent(g, Float.toString(Data[curIndex][i]), - ((int)CurAngle - 1), 
	                  BaseX + CIRCLE_W / 2, BaseY + CIRCLE_H / 2,
	                  CIRCLE_H / 2, CIRCLE_W / 2);
	      CurAngle += DataAngle;
        }
    }
    
    private void drawPercent(Graphics g, String ticmsg, 
                     int angle, int x, int y, int h, int w) 
    {
       double x1,y1,x2,y2;

       x1 = Math.cos((double)angle*(Math.PI/180))*w;
       y1 = Math.sin((double)angle*(Math.PI/180))*h;
       x2 = Math.cos((double)angle*(Math.PI/180))*(w+10);
       y2 = Math.sin((double)angle*(Math.PI/180))*(h+10);
      
       g.drawLine((int)x1+x,(int)y1+y,(int)x2+x,(int)y2+y);
      
       if(x2 > x1)
         g.drawString(ticmsg + "%",(int)x2+x,(int)(y2>y1 ? g.getFontMetrics().getHeight() + y2 + y : y2 + y));
       else
         g.drawString(ticmsg + "%",(int)x2+x-g.getFontMetrics().stringWidth(ticmsg),
                     (int)(y2>y1 ? g.getFontMetrics().getHeight() + y2 + y : y2 + y));
    }
  
    public void putTooltip(Graphics g)
    {
        if( ToolTip != null && ToolTip != "" ){
          FontMetrics m = getFontMetrics(getFont());
		  int w = m.stringWidth( ToolTip );
		  int h = m.getHeight();
          int a = m.getAscent();

          int x = MouseX - 2;
          int y = MouseY - a - 2;
          g.setColor( new Color(181, 184, 219) );
          g.fill3DRect( x, y, w + 4, h + 4, true );
          g.setColor( Color.black );
          g.drawString( ToolTip, MouseX, MouseY );
        }
     }

    public void updateTooltip()
    {
       FontMetrics m = getFontMetrics(getFont());
       int h = m.getHeight();
       int a = m.getAscent();

       if( OldToolTip != null && OldToolTip != "" ){
          int w = m.stringWidth( OldToolTip );
          repaint( OldMouseX - 2, OldMouseY - a - 2, w + 4, h + 4 );
       }
       
       if( ToolTip != "" ){
         int w = m.stringWidth( ToolTip );
         repaint( MouseX - 2, MouseY - a - 2, w + 4, h + 4 );
       }

       OldMouseX = MouseX;
       OldMouseY = MouseY;
       OldToolTip = ToolTip;
    }

    // 지정된 각도가 어느 항목에 속하는지 알아낸다.
    public int GetItemIndex( int x, int y, float Data[] )
    {
        double sa = Math.atan2( (double)y, (double)x );
        sa = sa / Math.PI * 180;
        sa += 90;
        if( sa < 0 ) sa += 360;
        if( sa >= 360 ) sa -= 360;
      
        int count = ColItems.length;

        double ta = 0;
        for( int i = 0; i < count; i ++ ){
          ta += Data[i] * 3.6;
          if( ta >= sa ){
            return( i );
          }
        }
      
        return( 0 );
    }

    public void SetCurIndex( int index )
    {
        curIndex = index;
        repaint();
    }

    // 화면 다시 그리기
    public void start(Graphics g)
    {
        repaint();
    }
   
    public boolean mouseExit(Event e, int x, int y )
    {
        ToolTip = "";
        updateTooltip();

        return( true );
    }   

    public boolean mouseMove(Event e, int x, int y )
    {
        if( x >= BaseX && x < BaseX + CIRCLE_W &&
            y >= BaseY && y < BaseY + CircleH ){
          MouseX = x;
          MouseY = y;

          x -= BaseX;
          y -= BaseY;
          int i = GetItemIndex( x - CIRCLE_W/2, y - CircleH/2, Data[curIndex] );
          ToolTip = ColItems[i] + " : " + Data[curIndex][i] + "%";

          updateTooltip();
        } else {
          if( ToolTip != "" ){
            ToolTip = "";
            updateTooltip();
          }
        }
          
        return( true );
    }
    
    
   // BackImage drawing
   public void drawBackimage(Graphics g, Image img)
   {
      if (img != null){
        int w = size().width;
        int h = size().height;
        int iw = img.getWidth(this);
        int ih = img.getHeight(this);
      
        int lw = w / iw;
        if( w % iw > 0 ) lw ++;
        int lh = h / ih;
        if( h % ih > 0 ) lh ++;
      
        int x, y;
        y = 0;

        for( int i = 0; i < lh; i ++ ){
          x = 0;
          for( int j = 0; j < lw; j ++ ){
            g.drawImage(img, x, y, iw, ih, this);
            x += iw;
          }
          y += ih;
        }
      }
   }   
   
    // Applet의 실제적인 Main 프로시저
    public void paint(Graphics g)
    {   
        setBackground(Color.white);
        
        // 배경 이미지 넣기
        drawBackimage(g, img);
        
        // 데이터 넣기
        putXYData(g);
        putTooltip(g);
    }
}
