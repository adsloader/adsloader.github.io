// 으... 값을 던져주기 위한 클래스
// 왜 갑자기 설계를 바꾸게 해서 이런 뽀록을 사용해야 한단 말인가...?
// 화가 난다. 설계 자체를 바꾸는 요구사항은 없어져야 한다...
import java.awt.*;
public class forDummy 
{
   // 전달을 위한 클래스..
   String GraphTitle;
   String RowTitle, RowItems[];
   String ColTitle, ColItems[];
   float Data[][];

   Image titleGif;
   
   public void Metallica( String GraphTitle,
                          String RowTitle, String RowItems[],
                          String ColTitle, String ColItems[],
                          float Data[][], Image titleGif)
   {
       this.GraphTitle = GraphTitle;
       this.RowTitle = RowTitle; 
       this.RowItems = RowItems;
       this.ColTitle = ColTitle; 
       this.ColItems = ColItems;
       this.Data = Data; 
       this.titleGif = titleGif;
   }
}