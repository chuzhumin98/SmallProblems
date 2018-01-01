// plotcurve.cpp : 实现文件
//

#include "stdafx.h"
#include "bigJob.h"
#include "plotcurve.h"
#include "afxdialogex.h"
#include <cmath>


// plotcurve 对话框

IMPLEMENT_DYNAMIC(plotcurve, CDialogEx)

plotcurve::plotcurve(CWnd* pParent /*=NULL*/)
	: CDialogEx(plotcurve::IDD, pParent)
	, m_period(0)
{
	//默认周期数为1/4
	//this->m_Rscrollbar.SetScrollRange(0,255);
	//this->m_Rscrollbar.SetScrollPos(0);
}

plotcurve::~plotcurve()
{
}

BOOL plotcurve::OnInitDialog()
{
	CDialogEx::OnInitDialog();


	// TODO:  在此添加额外的初始化
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
	// 异常: OCX 属性页应返回 FALSE
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


// plotcurve 消息处理程序


void plotcurve::OnBnClickedRadioQ1()
{
	// TODO: 在此添加控件通知处理程序代码
	this->m_period = 0; //设置周期模式，m_period表示m_period/4个周期
}


void plotcurve::OnBnClickedRadioQ2()
{
	// TODO: 在此添加控件通知处理程序代码
	this->m_period = 1;
}


void plotcurve::OnBnClickedRadioQ3()
{
	// TODO: 在此添加控件通知处理程序代码
	this->m_period = 2;
}


void plotcurve::OnBnClickedRadioQ4()
{
	// TODO: 在此添加控件通知处理程序代码
	this->m_period = 3;
}


void plotcurve::OnBnClickedPlot()
{
	// TODO: 在此添加控件通知处理程序代码

	CClientDC dc(this);
	CPen hpen;
	CBrush brushBk;
	CRect rectClient;
	GetClientRect(&rectClient);
	//定义作图位置
	const int left = rectClient.right - 330;
	const int top = 20;
	const int right = rectClient.right - 30;
	const int bottom = 274;
	//定义作图区域
	CRect rectDraw(left, top, right, bottom);
	brushBk.CreateSolidBrush(RGB(255,255,255));
	//选入白色背景刷
	dc.SelectObject(&brushBk);
	dc.Rectangle(rectDraw);

	int A = (int) ((bottom - top) / 2 * 0.95); //确定振幅
	int centerY = (bottom + top) / 2; //确定中心轴的Y轴坐标
	int arraySize = 101; //数组的大小
	int deltaX = 3; //数组中两项之间的横坐标差值
	POINT* points = new POINT [arraySize]; //建立一个点列数组

	/*
	对数组进行赋值
	*/
	double deltaFuctionX = (this->m_period + 1) * PI 
		/ 2.0 / (double)(arraySize-1); //在函数中的自变量的变化值
	for (int i = 0; i < arraySize; i++) {
		points[i].x = left + deltaX * i;
		points[i].y = (long)(centerY - sin(deltaFuctionX*i)*A);
	}
	int R = this->m_Rscrollbar.GetScrollPos();
	int G = this->m_Gslider.GetPos();
	CSpinButtonCtrl* pSpin =(CSpinButtonCtrl*) GetDlgItem(IDC_SPIN_B);
	int B = pSpin->GetPos(); //获取RGB颜色的值
	hpen.CreatePen(PS_SOLID , 3, RGB(R, G, B));
	dc.SelectObject(&hpen);
	dc.Polyline(points, arraySize);

	delete []points; //清空数组内存
}


void plotcurve::OnBnClickedCancel()
{
	// TODO: 在此添加控件通知处理程序代码
	CDialogEx::OnCancel();
}




int plotcurve::ChangeRInfo(int pos) {
	TCHAR sPos[10];
    _itow_s(pos,sPos,10);
	m_Rinfo.SetWindowText(sPos);
	UpdateData(FALSE);  //将与控件绑定的变量内容显示到屏幕上
	return 0;
}

int plotcurve::ChangeGInfo(int pos) {
	CString sPos;
	sPos.Format(L"%d", pos);
	m_Ginfo.SetSel(0,-1);
	m_Ginfo.ReplaceSel(sPos);
	UpdateData(FALSE);  //将与控件绑定的变量内容显示到屏幕上
	return 0;
}

void plotcurve::OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值
	int iNowPos;
	if(pScrollBar==&m_Rscrollbar) {
	int low = 0, high = 255; //滚动条的最大最小值设置
	int stepPageUp = 10; //pageup所增加的大小
	int stepUp = 1; //up所增加的大小
	CScrollBar* tmp = &m_Rscrollbar;
	   switch(nSBCode) {
	case SB_THUMBTRACK://拖动滚动滑块时
       tmp->SetScrollPos(nPos);
       break;
     case SB_LINEDOWN://单击滚动条向下的箭头
       iNowPos=tmp->GetScrollPos();
       iNowPos=iNowPos+stepUp;
	   if(iNowPos>high)           
            iNowPos=high;
       tmp->SetScrollPos(iNowPos);
       break;
	case SB_LINEUP:	//单击滚动条向上的箭头
		iNowPos=tmp->GetScrollPos();
		iNowPos=iNowPos-stepUp;
		if(iNowPos<low)
			iNowPos=low;
		tmp->SetScrollPos(iNowPos);
		break;

	case SB_PAGEDOWN://单击滚动条下面的箭头与滚动块之间区域
		iNowPos=tmp->GetScrollPos();
		iNowPos=iNowPos+stepPageUp;
		if(iNowPos>high)
			iNowPos=high;
		tmp->SetScrollPos(iNowPos);
		break;
	case SB_PAGEUP://单击滚动条上面的箭头与滚动块之间的区域
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
	// TODO: 在此添加消息处理程序代码和/或调用默认值
	if (pScrollBar->GetDlgCtrlID() == IDC_SPIN_B)
  { CString strValue; //定义一字符串对象存储编辑框中要显示的内容
    strValue.Format(L"%3d", (long) nPos);
   ((CSpinButtonCtrl*) pScrollBar)->GetBuddy()->SetWindowText(strValue);
  }


	CDialogEx::OnVScroll(nSBCode, nPos, pScrollBar);
}
