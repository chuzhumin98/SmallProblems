#pragma once
#include "afxwin.h"
#include "afxcmn.h"
#define PI 3.1415926535

// plotcurve 对话框

class plotcurve : public CDialogEx
{
	DECLARE_DYNAMIC(plotcurve)

public:
	plotcurve(CWnd* pParent = NULL);   // 标准构造函数
	virtual ~plotcurve();

// 对话框数据
	enum { IDD = IDD_DIALOG_PLOTCURVE };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV 支持

	DECLARE_MESSAGE_MAP()
public:
	int m_period;
	afx_msg void OnBnClickedRadioQ1();
	afx_msg void OnBnClickedRadioQ2();
	afx_msg void OnBnClickedRadioQ3();
	afx_msg void OnBnClickedRadioQ4();
	afx_msg void OnBnClickedPlot();
	afx_msg void OnBnClickedCancel();
	int ChangeRInfo(int pos = 0);
	int ChangeGInfo(int pos = 0);
	CScrollBar m_Rscrollbar;
	virtual BOOL OnInitDialog();
	CStatic m_Rinfo;
	afx_msg void OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar);
	CSliderCtrl m_Gslider;
	CEdit m_Ginfo;
	afx_msg void OnVScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar);
};
