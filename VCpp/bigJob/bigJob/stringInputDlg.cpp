// stringInputDlg.cpp : ʵ���ļ�
//

#include "stdafx.h"
#include "bigJob.h"
#include "stringInputDlg.h"
#include "afxdialogex.h"


// stringInputDlg �Ի���

IMPLEMENT_DYNAMIC(stringInputDlg, CDialogEx)

stringInputDlg::stringInputDlg(CWnd* pParent /*=NULL*/)
	: CDialogEx(stringInputDlg::IDD, pParent)
	, m_strInput(_T(""))
{

}

stringInputDlg::~stringInputDlg()
{
}

void stringInputDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialogEx::DoDataExchange(pDX);
	DDX_Text(pDX, IDC_EDIT_INPUT, m_strInput);
}


BEGIN_MESSAGE_MAP(stringInputDlg, CDialogEx)
END_MESSAGE_MAP()


// stringInputDlg ��Ϣ�������
