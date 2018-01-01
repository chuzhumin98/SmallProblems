
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
END_MESSAGE_MAP()

// CbigJobView 构造/析构

CbigJobView::CbigJobView()
{
	// TODO: 在此处添加构造代码

}

CbigJobView::~CbigJobView()
{
}

BOOL CbigJobView::PreCreateWindow(CREATESTRUCT& cs)
{
	// TODO: 在此处通过修改
	//  CREATESTRUCT cs 来修改窗口类或样式

	return CView::PreCreateWindow(cs);
}

// CbigJobView 绘制

void CbigJobView::OnDraw(CDC* /*pDC*/)
{
	CbigJobDoc* pDoc = GetDocument();
	ASSERT_VALID(pDoc);
	if (!pDoc)
		return;

	// TODO: 在此处为本机数据添加绘制代码
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
	OnContextMenu(this, point);
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
	plotcurve myPlot; //新建一个对话框对象
	myPlot.DoModal(); //打开这个对话框
}
