#pragma once
#include "CWMPPlayer4.h"

// MediaPlayDlg 对话框

class MediaPlayDlg : public CDialogEx
{
	DECLARE_DYNAMIC(MediaPlayDlg)

public:
	MediaPlayDlg(CWnd* pParent = NULL);   // 标准构造函数
	virtual ~MediaPlayDlg();

// 对话框数据
	enum { IDD = IDD_DIALOG_MEDIA };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV 支持

	DECLARE_MESSAGE_MAP()
public:
	CWMPPlayer4 m_mediaPlay;
	DECLARE_EVENTSINK_MAP()
	void DoubleClickOcx1(short nButton, short nShiftState, long fX, long fY);
};
