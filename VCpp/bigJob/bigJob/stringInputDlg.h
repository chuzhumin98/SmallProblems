#pragma once
#include "afxwin.h"


// stringInputDlg �Ի���

class stringInputDlg : public CDialogEx
{
	DECLARE_DYNAMIC(stringInputDlg)

public:
	stringInputDlg(CWnd* pParent = NULL);   // ��׼���캯��
	virtual ~stringInputDlg();

// �Ի�������
	enum { IDD = IDD_DIALOG_INPUT };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV ֧��

	DECLARE_MESSAGE_MAP()
public:
	CString m_strInput;
};
