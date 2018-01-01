// MediaPlayDlg.cpp : ʵ���ļ�
//

#include "stdafx.h"
#include "bigJob.h"
#include "MediaPlayDlg.h"
#include "afxdialogex.h"


// MediaPlayDlg �Ի���

IMPLEMENT_DYNAMIC(MediaPlayDlg, CDialogEx)

MediaPlayDlg::MediaPlayDlg(CWnd* pParent /*=NULL*/)
	: CDialogEx(MediaPlayDlg::IDD, pParent)
{

}

MediaPlayDlg::~MediaPlayDlg()
{
}

void MediaPlayDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialogEx::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_OCX1, m_mediaPlay);
}


BEGIN_MESSAGE_MAP(MediaPlayDlg, CDialogEx)
END_MESSAGE_MAP()


// MediaPlayDlg ��Ϣ�������
BEGIN_EVENTSINK_MAP(MediaPlayDlg, CDialogEx)
	ON_EVENT(MediaPlayDlg, IDC_OCX1, 6506, MediaPlayDlg::DoubleClickOcx1, VTS_I2 VTS_I2 VTS_I4 VTS_I4)
END_EVENTSINK_MAP()


void MediaPlayDlg::DoubleClickOcx1(short nButton, short nShiftState, long fX, long fY)
{
	// TODO: �ڴ˴������Ϣ����������
	m_mediaPlay.put_URL(L"myshuo.mp4");   
		 // ����ý���ļ���������������ʼ����
}
