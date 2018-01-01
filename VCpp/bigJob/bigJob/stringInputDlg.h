#pragma once
#include "afxwin.h"


// stringInputDlg 对话框

class stringInputDlg : public CDialogEx
{
	DECLARE_DYNAMIC(stringInputDlg)

public:
	stringInputDlg(CWnd* pParent = NULL);   // 标准构造函数
	virtual ~stringInputDlg();

// 对话框数据
	enum { IDD = IDD_DIALOG_INPUT };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV 支持

	DECLARE_MESSAGE_MAP()
public:
	CString m_strInput;
};
