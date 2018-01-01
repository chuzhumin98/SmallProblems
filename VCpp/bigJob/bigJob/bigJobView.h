
// bigJobView.h : CbigJobView ��Ľӿ�
//

#pragma once


class CbigJobView : public CView
{
protected: // �������л�����
	CbigJobView();
	DECLARE_DYNCREATE(CbigJobView)

// ����
public:
	CbigJobDoc* GetDocument() const;

// ����
public:

// ��д
public:
	virtual void OnDraw(CDC* pDC);  // ��д�Ի��Ƹ���ͼ
	virtual BOOL PreCreateWindow(CREATESTRUCT& cs);
protected:
	virtual BOOL OnPreparePrinting(CPrintInfo* pInfo);
	virtual void OnBeginPrinting(CDC* pDC, CPrintInfo* pInfo);
	virtual void OnEndPrinting(CDC* pDC, CPrintInfo* pInfo);

// ʵ��
public:
	virtual ~CbigJobView();
#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif

protected:

// ���ɵ���Ϣӳ�亯��
protected:
	afx_msg void OnFilePrintPreview();
	afx_msg void OnRButtonUp(UINT nFlags, CPoint point);
	afx_msg void OnContextMenu(CWnd* pWnd, CPoint point);
	afx_msg void OnOperFontChange(UINT nID);
	afx_msg void OnUpdateOperFontChange(CCmdUI * pCmdUI);

	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnOperDraw();

public:
	CString	m_strShow;	  // ��ʾ������

	afx_msg void OnOperInput();
	int m_nFontIndex;
};

#ifndef _DEBUG  // bigJobView.cpp �еĵ��԰汾
inline CbigJobDoc* CbigJobView::GetDocument() const
   { return reinterpret_cast<CbigJobDoc*>(m_pDocument); }
#endif

