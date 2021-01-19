//
//  ViewController.swift
//  Vibe Challenge
//
//  Created by Tony Dang on 17/12/2020.
//

import UIKit
import VibeChallengeSDK

protocol OnboardVCDelegate: class {
    func onboardVC(_ vc: OnboardVC, didLoginSuccessWith code: String)
}

class OnboardVC: UIViewController {
    weak var delegate: OnboardVCDelegate?
        
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if let controller = segue.destination as? ScreenLoginSlackVC {
            controller.delegate = self
        }
    }
}

extension OnboardVC: ScreenLoginSlackVCDelegate {
    
    func loginSuccess(_ vc: ScreenLoginSlackVC, didSuccess code: String) {
        delegate?.onboardVC(self, didLoginSuccessWith: code)
        vc.dismiss(animated: true, completion: nil)
    }
        
    func loginFailed(_ vc: ScreenLoginSlackVC, didFailed error: Error) {
        vc.dismiss(animated: true, completion: nil)
        let alertTitle = "login_failed_popup.title".localized
        let alertMessage = "login_failed_popup.message".localized
        let alert = UIAlertController(title: alertTitle, message: alertMessage, preferredStyle: .alert)
        
        let actionTitle = "general.ok".localized
        let action = UIAlertAction(title: actionTitle, style: .default, handler: nil)
        
        alert.addAction(action)
        
        present(alert, animated: true, completion: nil)
    }
}

