//
//  ScreenLoginSlack.swift
//  Vibe Challenge
//
//  Created by Nguyen Duc Thien on 22/12/2020.
//
import UIKit
import WebKit
import VibeChallengeSDK

protocol ScreenLoginSlackVCDelegate: class {
    func loginSuccess(_ vc: ScreenLoginSlackVC, didSuccess code: String)
    func loginFailed(_ vc: ScreenLoginSlackVC, didFailed error: Error)
}

private struct LoginResource {
    static let URLString = """
https://axonvibe.slack.com/oauth?client_id=2924833350.1575425842754&scope=identity.basic%2Cidentity.avatar&user_scope=&redirect_uri=&state=&granular_bot_scope=0&single_channel=0&install_redirect=&tracked=1&team=
"""
}

class ScreenLoginSlackVC: UIViewController, WKUIDelegate{
    
    @IBOutlet weak private var webView: WKWebView!
    weak var delegate: ScreenLoginSlackVCDelegate?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        webView.navigationDelegate = self
        
        guard let url = URL(string: LoginResource.URLString) else {
            return
        }
        let myRequest = URLRequest(url: url)
        webView.load(myRequest)
        webView.allowsBackForwardNavigationGestures = true
    }
    
    enum LoginError: Error {
        case failed
    }
}

extension ScreenLoginSlackVC: WKNavigationDelegate {
    
    func webView(_ webView: WKWebView, decidePolicyFor navigationAction: WKNavigationAction, decisionHandler: @escaping (WKNavigationActionPolicy) -> Void) {
        guard let url = navigationAction.request.url,
              let query = url.query else {
            decisionHandler(.allow)
            return
        }
        let param = query.components(separatedBy: "&")
        for parameter in param {
            if parameter.hasPrefix("code=") {
                decisionHandler(.cancel)
                delegate?.loginSuccess(self, didSuccess: parameter)
                return
            }
        }
        if query.contains("deny") {
            decisionHandler(.cancel)
            delegate?.loginFailed(self, didFailed: LoginError.failed)
            return
        }
        decisionHandler(.allow)
    }
}
