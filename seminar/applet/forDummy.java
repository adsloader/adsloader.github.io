// ��... ���� �����ֱ� ���� Ŭ����
// �� ���ڱ� ���踦 �ٲٰ� �ؼ� �̷� �Ƿ��� ����ؾ� �Ѵ� ���ΰ�...?
// ȭ�� ����. ���� ��ü�� �ٲٴ� �䱸������ �������� �Ѵ�...
import java.awt.*;
public class forDummy 
{
   // ������ ���� Ŭ����..
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