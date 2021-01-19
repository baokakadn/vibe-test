//
//  ScanVC.swift
//  Vibe Challenge
//
//  Created by Nguyen Duc Thien on 23/12/2020.
//

import UIKit
import VibeChallengeSDK

protocol UserSlackInforVCDelegate: class {
    func userSlackInforVCDidTapLogout(_ vc: UserSlackInforVC)
}

class UserSlackInforVC: UIViewController {
    @IBOutlet weak private var name: UILabel!
    @IBOutlet weak private var image: UIImageView!
    
    weak var delegate: UserSlackInforVCDelegate?
    
    let viewModel = UserSlackInforVM()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setupViewModel()
    }
    
    private func setupViewModel() {
        viewModel.displayName.observe { [weak self] name in
            self?.name.text = name
        }
        viewModel.avatarUrl.observe { [weak self] url in
            self?.image.loadURLString(url)
        }
    }

    @IBAction private func logout() {
        delegate?.userSlackInforVCDidTapLogout(self)
    }
}
