import java.awt.*;

//
// TextPanel
//

public class TextPanel extends Panel
{
  final int INSET_SIZE = 10;      // 여백 크기
  final int DOT_SIZE_WIDTH = 10;  // 도트의 수평크기
  final int DOT_SIZE_HEIGHT = 5;  // 도트의 수직크기
  final int DOT_GAP = 10;         // 도트와 문자 사이의 공백
  final int STR_GAP_HEIGHT = 4;   // 문자열 수직 간격
  final int TITLE_HEIGHT = 20;

  int ClientWidth = INSET_SIZE * 2;
  int ClientHeight = TITLE_HEIGHT + INSET_SIZE * 2;

  String Title = "";
  int ItemMax;
  int ItemCount;
  int MxWidth = 0;
  String Item[];
  Color  ItemColor[];
  Color backColor = new Color(225, 233, 221);
  int ClickX, ClickY;
  boolean mDown = false;
  Container Parent;

  public TextPanel( Container parent, int ItemLimit )
  {
      ItemCount = 0;
      Item = new String[ ItemLimit ];
      ItemColor = new Color[ ItemLimit ];

      ItemMax = ItemLimit;

      Font f = new Font( "바탕체", Font.BOLD, 12 );
      setFont( f );

      Parent = parent;

      resize( INSET_SIZE*2, INSET_SIZE*2 );
  }

  private void SetWidth( int width )
  {
      ClientWidth  = width + INSET_SIZE*2 + DOT_SIZE_WIDTH + DOT_GAP;
      resize( ClientWidth, ClientHeight );
  }
  private void SetHeight( int height )
  {
      ClientHeight = height + INSET_SIZE*2 + TITLE_HEIGHT;
      resize( ClientWidth, ClientHeight );
  }

  public void SetSize( int cx, int cy )
  {
      SetWidth( cx );
      SetHeight( cy );
  }

  public void SetTitle( String title )
  {
      this.Title = title;

      FontMetrics m = getFontMetrics(getFont());

      int tw = m.stringWidth( title );

      if( tw > MxWidth + DOT_SIZE_WIDTH + DOT_GAP ){
        SetWidth( tw );
      }
  }

  public boolean Add( Color col, String ItemStr )
  {
      if( ItemCount < ItemMax ){
        Item[ItemCount] = ItemStr;
        ItemColor[ItemCount] = col;
        ItemCount ++;

        FontMetrics m = getFontMetrics(getFont());
        int width = m.stringWidth( ItemStr );
        int height= m.getHeight();

        if( width > MxWidth ){
          MxWidth = width;
        }

        SetSize( MxWidth, height * ItemCount );
        SetTitle( Title );
        repaint();

        return( true );
      } else
        return( false );
  }

  public void Clear()
  {
      for( int i = 0; i < ItemCount; i ++ ){
        Item[i] = "";
      }
      ItemCount = 0;

      resize( INSET_SIZE*2, INSET_SIZE*2 );
      repaint();
  }
  
  public void setBackColor(Color color)
  {
     backColor = color;
  }
  
  public void paint(Graphics g)
  {
      // 클라이언트 영역을 그린다.
      Dimension r = size();
      g.setColor( backColor );
      g.fill3DRect( 0, 0, r.width, r.height, true);

      // 타이틀 바를 그린다.
      g.setColor( Color.blue );
      g.fill3DRect( 2, 2, r.width-4, TITLE_HEIGHT, true);

      // 폰트 정보를 얻는다.
      FontMetrics m = getFontMetrics(getFont());
      int height =  m.getHeight();
      int ascent = m.getAscent();

      g.setColor( Color.white );
      int tw = m.stringWidth( Title );
      int tx = (r.width - tw) / 2;
      int ty = 2 + (TITLE_HEIGHT - height) / 2 + ascent;
      g.drawString( Title, tx, ty );

      int yp = INSET_SIZE + TITLE_HEIGHT;

      for( int i = 0; i < ItemCount; i ++ ){
        g.setColor( ItemColor[i] );
        g.fill3DRect( INSET_SIZE, yp + (height-DOT_SIZE_HEIGHT)/2,
                      DOT_SIZE_WIDTH, DOT_SIZE_HEIGHT, true );
        g.drawString( Item[i], INSET_SIZE + DOT_SIZE_WIDTH + DOT_GAP, yp + ascent );
        yp += height;
      }
  }

  public boolean mouseDown( Event e, int x, int y )
  {
      ClickX = x;
      ClickY = y;
      mDown = true;

      return( super.mouseDown( e, x, y ) );
  }

  public boolean mouseDrag( Event e, int x, int y )
  {
      if( mDown ){
        Point p = location();
        Dimension s = size();
        Dimension ps = Parent.size();

        int xx = x + p.x - ClickX;
        int yy = y + p.y - ClickY;

        if( xx < 0 ) xx = 0;
        if( yy < 0 ) yy = 0;
        int t = ps.width - s.width;
        if( xx > t ) xx = t;
        t = ps.height - s.height;
        if( yy > t ) yy = t;

        mDown = false;
        move( xx, yy );
      } else
        mDown = true;

      return( super.mouseDrag( e, x, y ) );
  }
}
