#pragma once
#include "CWMPPlayer4.h"

// MediaPlayDlg �Ի���

class MediaPlayDlg : public CDialogEx
{
	DECLARE_DYNAMIC(MediaPlayDlg)

public:
	MediaPlayDlg(CWnd* pParent = NULL);   // ��׼���캯��
	virtual ~MediaPlayDlg();

// �Ի�������
	enum { IDD = IDD_DIALOG_MEDIA };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV ֧��

	DECLARE_MESSAGE_MAP()
public:
	CWMPPlayer4 m_mediaPlay;
	DECLARE_EVENTSINK_MAP()
	void DoubleClickOcx1(short nButton, short nShiftState, long fX, long fY);
};
