package pac1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Vector;



/**实验报告************************************************************
 * 1、摘要
 * 2、原理
 * 3、方法细节
 * 4、实验数据，额外资源
 * 5,算法复杂度
 * 6、结论
 * 7、参考文献
 *********************************************************************************/

/****************************************************************************************************
 * **************************************************************************************************
 *@author 阿杜、玉消耗                                                                                                                                                                                                                              **
 *@since  2017                                                                                     **
 *@说明	  1、文件路径dict、traning_result、traning_data、mydict_1_path放在程序文件路径即可。		               **
 *		  2、分词函数Segment_1和Segment的时间效率根据词典和文本规模和结果不同而有稍微时间差别，但结果一样可自行选择。                             **
 *		  3、符号集symbol可以添加符号，分词是一每两个符号之间作为参数。                                                                                                                            **
 *        4、若要调整分词条件，Segment_1（或Segmen）函数在if( dict.contains(temp))语句后面添加或修改条件即可。                      **
 *                                                                                                 **
 *@词典介绍 1、dict.txt是老师提供的词库文件共包含35万个词。                                                                                                                                                   **
 *		  2、mydict_1_path.txt是自己从搜狗输入法词库中下载并整理的词典（虽然有一些特殊符号，单大部分是汉语词语），共包含4亿词语。     **
 *                                                                                                 **
 * **************************************************************************************************
 ****************************************************************************************************/

public class Segregate {
	
	//每次处理的字符串（一般为一行）
	public String string_line;
		
	//符号集
	Vector<String> symbol=new Vector<>();
		
	//词典
	public Vector<String> dict=new Vector<>();
	
	//词典中最长的词语
	int max_dict_len;
	
	//分词结果文件路径
	String training_result="training_result_4.txt";
	File training_result_path=new File(training_result);
	
	//词典文件路径
	String dict_path="dict.txt";
	
	//待处理文本的路径
	String training_data_path="test.txt";
	
	//我的自加词典的文件路径
	String mydict_1_path="dict1.txt";
	
		
	
	/***************************************************************************************************
	 * *************************************************************************************************
	 * * 函数名：Segregate -- 构造函数
	 * * 参数：无
	 * * 返回值：无
	 * * 功能：（1）初始化分段符号集；（2）从文中读取词典，初始化词典容器dict；
	 * * 注：（1）符号集可以增加；
	 * *    （2）词典分别从两个文件dict.txt(老师提供的词典文本）和mydict_1.txt（自己增加的词典）读取；
	 * ************************************************************************************************
	 * ************************************************************************************************/
	public Segregate() {
		
		 /***
		 * 1-初始化段落符号集,下列任意两个符号之间的文本为一段可分词段落。                                                             
		 * 2-可根据情况增加符号。                                                                                            					    
		 ***/
		symbol.add("，");
		symbol.add(" ");
		symbol.add("！");
		symbol.add("。");
		symbol.add(",");
		symbol.add("?");
		symbol.add("？");
		symbol.add("!");
		symbol.add("、");
		symbol.add("：");
		symbol.add("·");
		
		
		//初始化词典
		max_dict_len=1;
		File dictionary=new File(dict_path);
		try {
			FileReader fReader1=new FileReader(dictionary);
			BufferedReader bReader1=new BufferedReader(fReader1);
			String temp0=null;
			long line_index=0;
			while((temp0=bReader1.readLine())!=null)
			{
				dict.add(temp0);
				line_index++;	
				if(temp0.length()>=max_dict_len)
				{
					max_dict_len=temp0.length();
				}
			}
			bReader1.close();
			if(line_index!=0)
			{
				System.out.println("词典读取完毕！");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		
		/*//我增加的词典，有数数量庞大运行时会需要很长时间
	   File dictionary_1=new File(mydict_1_path);
		try {
			FileReader fReader1=new FileReader(dictionary_1);
			BufferedReader bReader1=new BufferedReader(fReader1);
			String temp0=null;
			long line_index=0;
			while((temp0=bReader1.readLine())!=null)
			{
				if(temp0.length()<=2)
				{
					dict.add(temp0);
				}
				line_index++;
					
			}
			bReader1.close();
			System.out.println(line_index);
			if(line_index!=0)
			{
				System.out.println("我的词典读取完毕！");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
		
	}
	
	
	
	/*****************************************************************************************************
	 *  **************************************************************************************************
	 * 函数名：main(主函数）
	 * @param args
	 *  **************************************************************************************************
	 * **************************************************************************************************/
	public static void main(String[] args)
	{
		
		Segregate obj1=new Segregate();
		System.out.println("正在分词...");
		//读取待分词的文本,每次读取一行并交给     Section函数处理                     
		File file1=new File(obj1.training_data_path);
		try {            
			FileReader fr = new FileReader(file1);
		    BufferedReader bf=new BufferedReader(fr);
		    String string1="";
		    long line_index=0;
		    while((string1=bf.readLine())!=null)
		    {
		    	line_index++;	
		    	obj1.string_line=string1;
		    	obj1.Section();			//将一行文本按符号分成句子段落再进行分词
		    	
		    	//obj1.Segment(obj1.string_line);//直接对一整行进行分词
		    }
		    bf.close();
		    
		    if(line_index==0)
		    {
		    	
		    	System.out.println("未读取数据请检查文件"+obj1.training_data_path);
		    }     
		}catch (Exception e) {
			e.printStackTrace();}
	}
	
	
	
	/***************************************************************************************************
	 * ************************************************************************************************
	 * 函数名：Section
	 * 参数：无
	 * 返回值：无
	 * 功能：将一行文本按照分割符号分成一个个可分词的段落交给Segment函数进行分词
	 * 说明：
	 *  *************************************************************************************************
	 * *************************************************************************************************/
	public void Section()
	{
		String temp1="";
		for(int i=0;i<string_line.length();i++)
		{
			temp1+=string_line.charAt(i);
			String temp2=""+string_line.charAt(i);
			
			//当遇到一个符号时将付好之前的段落进行分词 ，若一行中没有符号，或者行最后没有句号的情况
			if(symbol.contains(temp2)==true|| (i==string_line.length()-1 && symbol.contains(temp2)==false))
			{
				Segment(temp1);
				temp1="";
			}			
		}	
		
		//每一行处理完输出换行符
		try {
			FileWriter fileWriter=new FileWriter(training_result_path, true);
			fileWriter.write("\n");
			fileWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	
	
	/***************************************************************************************************
	 ***************************************************************************************************
	 * 函数名：Segment
	 * 参数：介于两个符号之间的字符串s,s是是一个以符号结尾的句子
	 * 返回值：无
	 * 功能：根据词典dict，对接受的字符串进行分词并输出到文件traning_result.txt
	 * 说明:
	 * 根据词典分词
	 * *************************************************************************************************
	 **************************************************************************************************/
	public void Segment(String s)
	{	
		for(int i=s.length();i>0;i--)
		{
			//结束标志，当所有分完了
			if(s.length()==0)
			{
				break;
			}			
			/*******从最长的字符开始截取,分词条件如下*******************
			 * 1-这点字符串tmep在词典中时是个单词
			 * 2-或者就剩一个单词时不管在不在词典中切分处理
			 * 3-若temp时数字，可以分词
			 * 4-若是英文单词分词
			 * 4-数字加百分号
			 ************************************************/
			String temp=s.substring(0, i);
			if( 
				temp.length()==1 
				|| temp.matches("[0-9]+(.[0-9]+)+[%]") 
				|| temp.matches("[0-9]+(.[0-9]+)+[％]") 
				|| temp.matches("[0-9]+[％]")
				|| temp.matches("[0-9]+[%]")
				|| temp.matches("[－]+[0-9]+[℃]")
				|| temp.matches("[0-9]+[℃]")
				|| temp.matches("[0-9]+(.[0-9]+)") 
				|| temp.matches("[0-9]+") 
				|| temp.matches("[－]+[0-9]+(.[0-9]+)") 
				|| temp.matches("[－]+[0-9]+") 
				|| temp.matches("[0-9]+[年]") 
				|| temp.matches("[0-9]+[月]")
				|| temp.matches("[0-9]+[日]") 
				||temp.matches("[a-zA-Z]+") 
				||dict.contains(temp))
			{
				temp+="  ";
				try {
					FileWriter fileWriter=new FileWriter(training_result_path, true);
					fileWriter.write(temp);
					fileWriter.close();
				} catch (Exception e) {
					e.printStackTrace();
				} 
				s=s.substring(i);
				i=s.length()+1;				
			}				
		}
	}
	
	
	

	/**************************************************************************************************
	 * ************************************************************************************************
	 * 函数名：Segment_1
	 * 参数：介于两个符号之间的字符串s,s是是一个以符号结尾的句子
	 * 返回值：无
	 * 功能：根据词典dict，对接受的字符串进行分词并输出到文件traning_result.txt
	 * 说明:Segment方法的优化版，每次截取词典中最长词的长的字符串进行分词
	 * 根据词典分词
	 * ************************************************************************************************
	 **************************************************************************************************/
	public void Segment_1(String s)
	{
			for(int i=0;i<s.length();i++)
			{
				if(s.length()<=max_dict_len)
				{
					for(int j=s.length();j>0;j--)
					{
						//结束标志，当所有分完了
						if(s.length()==0)
						{
							break;
						}
										
						/*******从最长的字符开始截取,分词条件如下*******************
						 * 1-这点字符串tmep在词典中时是个单词
						 * 2-或者就剩一个单词时不管在不在词典中切分处理
						 * 3-若temp时数字，可以分词
						 * 4-若是英文单词分词
						 * 4-数字加百分号
						 ************************************************/
						String temp=s.substring(0, j);
						if( dict.contains(temp)
							||temp.length()==1 
							|| temp.matches("[0-9]+") 
							|| temp.matches("[0-9]+(.[0-9]+)") 
							|| temp.matches("[0-9]+(.[0-9]+)+[%]") 
							|| temp.matches("[0-9]+[%]")
							||temp.matches("[a-zA-Z]+")  )
						{
							temp+="  ";
							try {
								FileWriter fileWriter=new FileWriter(training_result_path, true);
								fileWriter.write(temp);
								fileWriter.close();
							} catch (Exception e) {
								e.printStackTrace();
							} 
							s=s.substring(j);
							j=s.length()+1;		
						}	
						
					}
				}
				else {
					String s1=s.substring(i, max_dict_len);		
					for(int j=s1.length();j>0;j--)
					{
										
						/*******从最长的字符开始截取,分词条件如下*******************
						 * 1-这点字符串tmep在词典中时是个单词
						 * 2-或者就剩一个单词时不管在不在词典中切分处理
						 * 3-若temp时数字，可以分词
						 * 4-若是英文单词分词
						 * 4-数字加百分号
						 ************************************************/
						String temp=s1.substring(0, j);
						if( dict.contains(temp)
							||temp.length()==1 
							|| temp.matches("[0-9]+") 
							|| temp.matches("[0-9]+(.[0-9]+)") 
							|| temp.matches("[0-9]+(.[0-9]+)+[%]") 
							|| temp.matches("[0-9]+[%]")
							||temp.matches("[a-zA-Z]+")  )
						{
							temp+="  ";
							try {
								FileWriter fileWriter=new FileWriter(training_result_path, true);
								fileWriter.write(temp);
								fileWriter.close();
							} catch (Exception e) {
								e.printStackTrace();
							} 
							s=s.substring(j+1);
							i=-1;	
							break;
						}	
					}
				}	
			}
	} 
}




