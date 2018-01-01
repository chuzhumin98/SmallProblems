
// bigJobView.cpp : CbigJobView ���ʵ��
//

#include "stdafx.h"
// SHARED_HANDLERS ������ʵ��Ԥ��������ͼ������ɸѡ�������
// ATL ��Ŀ�н��ж��壬�����������Ŀ�����ĵ����롣
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
	// ��׼��ӡ����
	ON_COMMAND(ID_FILE_PRINT, &CView::OnFilePrint)
	ON_COMMAND(ID_FILE_PRINT_DIRECT, &CView::OnFilePrint)
	ON_COMMAND(ID_FILE_PRINT_PREVIEW, &CbigJobView::OnFilePrintPreview)
	ON_WM_CONTEXTMENU()
	ON_WM_RBUTTONUP()
	ON_COMMAND(ID_OPER_DRAW, &CbigJobView::OnOperDraw)
END_MESSAGE_MAP()

// CbigJobView ����/����

CbigJobView::CbigJobView()
{
	// TODO: �ڴ˴���ӹ������

}

CbigJobView::~CbigJobView()
{
}

BOOL CbigJobView::PreCreateWindow(CREATESTRUCT& cs)
{
	// TODO: �ڴ˴�ͨ���޸�
	//  CREATESTRUCT cs ���޸Ĵ��������ʽ

	return CView::PreCreateWindow(cs);
}

// CbigJobView ����

void CbigJobView::OnDraw(CDC* /*pDC*/)
{
	CbigJobDoc* pDoc = GetDocument();
	ASSERT_VALID(pDoc);
	if (!pDoc)
		return;

	// TODO: �ڴ˴�Ϊ����������ӻ��ƴ���
}


// CbigJobView ��ӡ


void CbigJobView::OnFilePrintPreview()
{
#ifndef SHARED_HANDLERS
	AFXPrintPreview(this);
#endif
}

BOOL CbigJobView::OnPreparePrinting(CPrintInfo* pInfo)
{
	// Ĭ��׼��
	return DoPreparePrinting(pInfo);
}

void CbigJobView::OnBeginPrinting(CDC* /*pDC*/, CPrintInfo* /*pInfo*/)
{
	// TODO: ��Ӷ���Ĵ�ӡǰ���еĳ�ʼ������
}

void CbigJobView::OnEndPrinting(CDC* /*pDC*/, CPrintInfo* /*pInfo*/)
{
	// TODO: ��Ӵ�ӡ����е��������
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


// CbigJobView ���

#ifdef _DEBUG
void CbigJobView::AssertValid() const
{
	CView::AssertValid();
}

void CbigJobView::Dump(CDumpContext& dc) const
{
	CView::Dump(dc);
}

CbigJobDoc* CbigJobView::GetDocument() const // �ǵ��԰汾��������
{
	ASSERT(m_pDocument->IsKindOf(RUNTIME_CLASS(CbigJobDoc)));
	return (CbigJobDoc*)m_pDocument;
}
#endif //_DEBUG


// CbigJobView ��Ϣ�������


void CbigJobView::OnOperDraw()
{
	// TODO: �ڴ���������������
	plotcurve myPlot; //�½�һ���Ի������
	myPlot.DoModal(); //������Ի���
}
