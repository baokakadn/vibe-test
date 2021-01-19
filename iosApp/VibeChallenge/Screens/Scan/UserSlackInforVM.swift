//
//  UserSlackInforVM.swift
//  VibeChallenge
//
//  Created by Thien Nguyen Duc on 06/01/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import VibeChallengeSDK

class UserSlackInforVM {
    private var profile: Profile?
    
    let displayName = Observable<String>()
    let avatarUrl = Observable<String>()
    
    func load(profile: Profile) {
        self.profile = profile
        displayName.updateValue(profile.user.name)
        avatarUrl.updateValue(profile.user.avatar)
    }
}
