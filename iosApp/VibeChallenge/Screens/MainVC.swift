//
//  MainVC.swift
//  VibeChallenge
//
//  Created by Thien Nguyen Duc on 30/12/2020.
//  Copyright © 2020 orgName. All rights reserved.
//

import UIKit
import VibeChallengeSDK

class MainVC: UIViewController {
    @IBOutlet private weak var onBoardViewContainer: UIView!
    @IBOutlet private weak var scanViewContainer: UIView!
    
    var profileVM = ProfileViewModel()
    
    private weak var scanVC: UserSlackInforVC!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setupViewModel()
        profileVM.getSavedProfile()
    }
    
    private func setupViewModel() {
        profileVM.profileLiveData.addObserver { [weak self] mCurrentState in
            guard let self = self, let mCurrentState = mCurrentState else { return }
            switch mCurrentState {
            case let state as SuccessGetProfileState:
                let response = state.response as? ResponseSuccess
                self.updateProfile(profile: response?.data)
            case is LogoutProfileState:
                self.updateProfile(profile: nil)
            case is ErrorGetProfileState:
                self.updateProfile(profile: nil)
            default:
                break
            }
        }
    }
    
    private func updateProfile(profile: Profile?) {
        if let profile = profile {
            scanVC.viewModel.load(profile: profile)
            self.onBoardViewContainer.isHidden = true
            self.scanViewContainer.isHidden = false
        } else {
            self.onBoardViewContainer.isHidden = false
            self.scanViewContainer.isHidden = true
            return
        }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        super.prepare(for: segue, sender: sender)
        switch segue.destination {
        case let controller as OnboardVC:
            controller.delegate = self
        case let controller as UserSlackInforVC:
            scanVC = controller
            controller.delegate = self
        default:
            break
        }
    }
}

extension MainVC: OnboardVCDelegate, UserSlackInforVCDelegate{
    func userSlackInforVCDidTapLogout(_ vc: UserSlackInforVC) {
        profileVM.logoutProfile()
    }
    
    func onboardVC(_ vc: OnboardVC, didLoginSuccessWith code: String) {
        profileVM.getProfile(code: code)
    }
}
