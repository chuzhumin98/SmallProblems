// plotcurve.cpp : ʵ���ļ�
//

#include "stdafx.h"
#include "bigJob.h"
#include "plotcurve.h"
#include "afxdialogex.h"
#include <cmath>


// plotcurve �Ի���

IMPLEMENT_DYNAMIC(plotcurve, CDialogEx)

plotcurve::plotcurve(CWnd* pParent /*=NULL*/)
	: CDialogEx(plotcurve::IDD, pParent)
	, m_period(0)
{
	//Ĭ��������Ϊ1/4
	//this->m_Rscrollbar.SetScrollRange(0,255);
	//this->m_Rscrollbar.SetScrollPos(0);
}

plotcurve::~plotcurve()
{
}

BOOL plotcurve::OnInitDialog()
{
	CDialogEx::OnInitDialog();


	// TODO:  �ڴ���Ӷ���ĳ�ʼ��
	this->m_Rscrollbar.SetScrollRange(0,255);
	this->m_Rscrollbar.SetScrollPos(0);
	this->ChangeRInfo();

	this->m_Gslider.SetRange(0,255);
	this->m_Gslider.SetPos(0);
	this->ChangeGInfo();

	CSpinButtonCtrl* pSpin =(CSpinButtonCtrl*) GetDlgItem(IDC_SPIN_B);
	pSpin->SetRange(0, 255);
	pSpin->SetPos(0);
	pSpin->GetBuddy()->SetWindowText(L"0");


	return TRUE;  // return TRUE unless you set the focus to a control
	// �쳣: OCX ����ҳӦ���� FALSE
}

void plotcurve::DoDataExchange(CDataExchange* pDX)
{
	CDialogEx::DoDataExchange(pDX);
	DDX_Radio(pDX, IDC_RADIO_Q1, m_period);
	DDX_Control(pDX, IDC_SCROLLBAR_R, m_Rscrollbar);
	DDX_Control(pDX, IDC_STATIC_R, m_Rinfo);
	DDX_Control(pDX, IDC_SLIDER_G, m_Gslider);
	DDX_Control(pDX, IDC_EDIT_G, m_Ginfo);
}


BEGIN_MESSAGE_MAP(plotcurve, CDialogEx)
	ON_BN_CLICKED(IDC_RADIO_Q1, &plotcurve::OnBnClickedRadioQ1)
	ON_BN_CLICKED(IDC_RADIO_Q2, &plotcurve::OnBnClickedRadioQ2)
	ON_BN_CLICKED(IDC_RADIO_Q3, &plotcurve::OnBnClickedRadioQ3)
	ON_BN_CLICKED(IDC_RADIO_Q4, &plotcurve::OnBnClickedRadioQ4)
	ON_BN_CLICKED(ID_PLOT, &plotcurve::OnBnClickedPlot)
	ON_BN_CLICKED(IDCANCEL, &plotcurve::OnBnClickedCancel)
	ON_WM_HSCROLL()
	ON_WM_VSCROLL()
END_MESSAGE_MAP()


// plotcurve ��Ϣ�������


void plotcurve::OnBnClickedRadioQ1()
{
	// TODO: �ڴ���ӿؼ�֪ͨ����������
	this->m_period = 0; //��������ģʽ��m_period��ʾm_period/4������
}


void plotcurve::OnBnClickedRadioQ2()
{
	// TODO: �ڴ���ӿؼ�֪ͨ����������
	this->m_period = 1;
}


void plotcurve::OnBnClickedRadioQ3()
{
	// TODO: �ڴ���ӿؼ�֪ͨ����������
	this->m_period = 2;
}


void plotcurve::OnBnClickedRadioQ4()
{
	// TODO: �ڴ���ӿؼ�֪ͨ����������
	this->m_period = 3;
}


void plotcurve::OnBnClickedPlot()
{
	// TODO: �ڴ���ӿؼ�֪ͨ����������

	CClientDC dc(this);
	CPen hpen;
	CBrush brushBk;
	CRect rectClient;
	GetClientRect(&rectClient);
	//������ͼλ��
	const int left = rectClient.right - 330;
	const int top = 20;
	const int right = rectClient.right - 30;
	const int bottom = 274;
	//������ͼ����
	CRect rectDraw(left, top, right, bottom);
	brushBk.CreateSolidBrush(RGB(255,255,255));
	//ѡ���ɫ����ˢ
	dc.SelectObject(&brushBk);
	dc.Rectangle(rectDraw);

	int A = (int) ((bottom - top) / 2 * 0.95); //ȷ�����
	int centerY = (bottom + top) / 2; //ȷ���������Y������
	int arraySize = 101; //����Ĵ�С
	int deltaX = 3; //����������֮��ĺ������ֵ
	POINT* points = new POINT [arraySize]; //����һ����������

	/*
	��������и�ֵ
	*/
	double deltaFuctionX = (this->m_period + 1) * PI 
		/ 2.0 / (double)(arraySize-1); //�ں����е��Ա����ı仯ֵ
	for (int i = 0; i < arraySize; i++) {
		points[i].x = left + deltaX * i;
		points[i].y = (long)(centerY - sin(deltaFuctionX*i)*A);
	}
	int R = this->m_Rscrollbar.GetScrollPos();
	int G = this->m_Gslider.GetPos();
	CSpinButtonCtrl* pSpin =(CSpinButtonCtrl*) GetDlgItem(IDC_SPIN_B);
	int B = pSpin->GetPos(); //��ȡRGB��ɫ��ֵ
	hpen.CreatePen(PS_SOLID , 3, RGB(R, G, B));
	dc.SelectObject(&hpen);
	dc.Polyline(points, arraySize);

	delete []points; //��������ڴ�
}


void plotcurve::OnBnClickedCancel()
{
	// TODO: �ڴ���ӿؼ�֪ͨ����������
	CDialogEx::OnCancel();
}




int plotcurve::ChangeRInfo(int pos) {
	TCHAR sPos[10];
    _itow_s(pos,sPos,10);
	m_Rinfo.SetWindowText(sPos);
	UpdateData(FALSE);  //����ؼ��󶨵ı���������ʾ����Ļ��
	return 0;
}

int plotcurve::ChangeGInfo(int pos) {
	CString sPos;
	sPos.Format(L"%d", pos);
	m_Ginfo.SetSel(0,-1);
	m_Ginfo.ReplaceSel(sPos);
	UpdateData(FALSE);  //����ؼ��󶨵ı���������ʾ����Ļ��
	return 0;
}

void plotcurve::OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar)
{
	// TODO: �ڴ������Ϣ�����������/�����Ĭ��ֵ
	int iNowPos;
	if(pScrollBar==&m_Rscrollbar) {
	int low = 0, high = 255; //�������������Сֵ����
	int stepPageUp = 10; //pageup�����ӵĴ�С
	int stepUp = 1; //up�����ӵĴ�С
	CScrollBar* tmp = &m_Rscrollbar;
	   switch(nSBCode) {
	case SB_THUMBTRACK://�϶���������ʱ
       tmp->SetScrollPos(nPos);
       break;
     case SB_LINEDOWN://�������������µļ�ͷ
       iNowPos=tmp->GetScrollPos();
       iNowPos=iNowPos+stepUp;
	   if(iNowPos>high)           
            iNowPos=high;
       tmp->SetScrollPos(iNowPos);
       break;
	case SB_LINEUP:	//�������������ϵļ�ͷ
		iNowPos=tmp->GetScrollPos();
		iNowPos=iNowPos-stepUp;
		if(iNowPos<low)
			iNowPos=low;
		tmp->SetScrollPos(iNowPos);
		break;

	case SB_PAGEDOWN://��������������ļ�ͷ�������֮������
		iNowPos=tmp->GetScrollPos();
		iNowPos=iNowPos+stepPageUp;
		if(iNowPos>high)
			iNowPos=high;
		tmp->SetScrollPos(iNowPos);
		break;
	case SB_PAGEUP://��������������ļ�ͷ�������֮�������
		iNowPos=tmp->GetScrollPos();
		iNowPos=iNowPos-stepPageUp;
		if(iNowPos<low)
          iNowPos=low;
		tmp->SetScrollPos(iNowPos);
		break;
	}
	   ChangeRInfo(tmp->GetScrollPos());
	}

	if(pScrollBar->GetDlgCtrlID() == IDC_SLIDER_G) {
		int nPos = this->m_Gslider.GetPos();
		this->ChangeGInfo(nPos);
	}

	CDialogEx::OnHScroll(nSBCode, nPos, pScrollBar);
}




void plotcurve::OnVScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar)
{
	// TODO: �ڴ������Ϣ�����������/�����Ĭ��ֵ
	if (pScrollBar->GetDlgCtrlID() == IDC_SPIN_B)
  { CString strValue; //����һ�ַ�������洢�༭����Ҫ��ʾ������
    strValue.Format(L"%3d", (long) nPos);
   ((CSpinButtonCtrl*) pScrollBar)->GetBuddy()->SetWindowText(strValue);
  }


	CDialogEx::OnVScroll(nSBCode, nPos, pScrollBar);
}
