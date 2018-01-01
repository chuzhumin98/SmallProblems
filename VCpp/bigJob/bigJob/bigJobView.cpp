
// bigJobView.cpp : CbigJobView 类的实现
//

#include "stdafx.h"
// SHARED_HANDLERS 可以在实现预览、缩略图和搜索筛选器句柄的
// ATL 项目中进行定义，并允许与该项目共享文档代码。
#ifndef SHARED_HANDLERS
#include "bigJob.h"
#include "plotcurve.h"
#endif

#include "bigJobDoc.h"
#include "bigJobView.h"
#include "stringInputDlg.h"
#include "MediaPlayDlg.h"


#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// CbigJobView

IMPLEMENT_DYNCREATE(CbigJobView, CView)

BEGIN_MESSAGE_MAP(CbigJobView, CView)
	// 标准打印命令
	ON_COMMAND(ID_FILE_PRINT, &CView::OnFilePrint)
	ON_COMMAND(ID_FILE_PRINT_DIRECT, &CView::OnFilePrint)
	ON_COMMAND(ID_FILE_PRINT_PREVIEW, &CbigJobView::OnFilePrintPreview)
	ON_WM_CONTEXTMENU()
	ON_WM_RBUTTONUP()
	ON_COMMAND(ID_OPER_DRAW, &CbigJobView::OnOperDraw)
	ON_COMMAND(ID_OPER_INPUT, &CbigJobView::OnOperInput)
	ON_COMMAND_RANGE(ID_OPER_LARGE,ID_OPER_SMALL,OnOperFontChange)	
		//字体大小变化的消息处理函数
	ON_UPDATE_COMMAND_UI_RANGE(ID_OPER_LARGE,ID_OPER_SMALL,OnUpdateOperFontChange)
		//字体大小变化时菜单子项变化的消息处理函数



		ON_WM_RBUTTONDOWN()
END_MESSAGE_MAP()

// CbigJobView 构造/析构

CbigJobView::CbigJobView()
	: m_nFontIndex(1)
{
	// TODO: 在此处添加构造代码
	//默认采用了中等字号
	m_strShow =L"Visual C++很容易学！";
	m_PopMenu.LoadMenu(IDR_POP_SHOW);  	// 创建并加载菜单资源


}

CbigJobView::~CbigJobView()
{
	m_PopMenu.DestroyMenu();              // 释放菜单资源    
}

BOOL CbigJobView::PreCreateWindow(CREATESTRUCT& cs)
{
	// TODO: 在此处通过修改
	//  CREATESTRUCT cs 来修改窗口类或样式

	return CView::PreCreateWindow(cs);
}

// CbigJobView 绘制

void CbigJobView::OnDraw(CDC* pDC)
{
	CbigJobDoc* pDoc = GetDocument();
	ASSERT_VALID(pDoc);
	if (!pDoc)
		return;

	// TODO: 在此处为本机数据添加绘制代码
	int fontSize = 10;
	if (this->m_nFontIndex == 1) {
		fontSize = 25;
	} else if (this->m_nFontIndex == 0) {
		fontSize = 50;
	}
	HFONT hfont =CreateFont			//创建自定义字体
		(fontSize,		//字体的高度
		 0,		//由系统根据高宽比选取字体最佳宽度值
		 0,		//文本的倾斜度为0，表示水平 
		 0,		//字体与基线的倾斜度为0， 表示与基线平行
		 FW_HEAVY,		//字体的粗度，FW_HEAVY为最粗
		 0,			//非斜体字
		 0,			//无下划线
		 0,			//无删除线
		 GB2312_CHARSET,	//表示所用的字符集为ANSI_CHARSET
		 OUT_DEFAULT_PRECIS,		//输出精度为默认精度
		 CLIP_DEFAULT_PRECIS,		//剪裁精度为默认精度
		 DEFAULT_QUALITY,		//输出质量为默认值
		 DEFAULT_PITCH|FF_DONTCARE,	//字间距和字体系列使用默认值
		 L"粗体字"			//字体名称
		);
	pDC->SelectObject(hfont);
	pDC->TextOut(100,100,m_strShow);   // 输出字符串

}


// CbigJobView 打印


void CbigJobView::OnFilePrintPreview()
{
#ifndef SHARED_HANDLERS
	AFXPrintPreview(this);
#endif
}

BOOL CbigJobView::OnPreparePrinting(CPrintInfo* pInfo)
{
	// 默认准备
	return DoPreparePrinting(pInfo);
}

void CbigJobView::OnBeginPrinting(CDC* /*pDC*/, CPrintInfo* /*pInfo*/)
{
	// TODO: 添加额外的打印前进行的初始化过程
}

void CbigJobView::OnEndPrinting(CDC* /*pDC*/, CPrintInfo* /*pInfo*/)
{
	// TODO: 添加打印后进行的清理过程
}

void CbigJobView::OnRButtonUp(UINT /* nFlags */, CPoint point)
{
	ClientToScreen(&point);
	//OnContextMenu(this, point);
}

void CbigJobView::OnContextMenu(CWnd* /* pWnd */, CPoint point)
{
#ifndef SHARED_HANDLERS
	theApp.GetContextMenuManager()->ShowPopupMenu(IDR_POPUP_EDIT, point.x, point.y, this, TRUE);
#endif
}


// CbigJobView 诊断

#ifdef _DEBUG
void CbigJobView::AssertValid() const
{
	CView::AssertValid();
}

void CbigJobView::Dump(CDumpContext& dc) const
{
	CView::Dump(dc);
}

CbigJobDoc* CbigJobView::GetDocument() const // 非调试版本是内联的
{
	ASSERT(m_pDocument->IsKindOf(RUNTIME_CLASS(CbigJobDoc)));
	return (CbigJobDoc*)m_pDocument;
}
#endif //_DEBUG


// CbigJobView 消息处理程序


void CbigJobView::OnOperDraw()
{
	// TODO: 在此添加命令处理程序代码
	MediaPlayDlg media;
	media.DoModal();
	plotcurve myPlot; //新建一个对话框对象
	myPlot.DoModal(); //打开这个对话框
}


void CbigJobView::OnOperInput()
{
	// TODO: 在此添加命令处理程序代码
	stringInputDlg dlgInput;	// 声明对话框变量
	if(dlgInput.DoModal() == IDOK) // 若点击OK按钮
    {	
		m_strShow = dlgInput.m_strInput;	// 更改字符串
		Invalidate();	                		// 强制重绘
    }

}

afx_msg void CbigJobView::OnOperFontChange(UINT nID) {
	m_nFontIndex = nID-ID_OPER_LARGE;
	Invalidate(); 
}

afx_msg void CbigJobView::OnUpdateOperFontChange(CCmdUI * pCmdUI) {
	pCmdUI->SetRadio(m_nFontIndex==(pCmdUI->m_nID - ID_OPER_LARGE));
}

void CbigJobView::OnRButtonDown(UINT nFlags, CPoint point)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值
	m_pPop = m_PopMenu.GetSubMenu(0);    // 获得第一个子菜单
	m_pPop->CheckMenuRadioItem(ID_OPER_LARGE,
            ID_OPER_SMALL,ID_OPER_LARGE+m_nFontIndex,
            MF_BYCOMMAND);//在颜色菜单项上加上选中标识
	ClientToScreen(&point); 	// 将坐标由客户坐标转化为屏幕坐标
	m_pPop->TrackPopupMenu(TPM_LEFTALIGN, point.x,point.y,this);//显示Pop-up菜单


	//CView::OnRButtonDown(nFlags, point); //禁用默认响应
}
