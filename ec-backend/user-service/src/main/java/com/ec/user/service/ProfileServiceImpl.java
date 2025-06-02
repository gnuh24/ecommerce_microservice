package com.ec.user.service;

import com.ec.user.dto.profile.ProfileCreateForm;
import com.ec.user.entity.Account;
import com.ec.user.entity.Profile;
import com.ec.user.repository.ProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ModelMapper modelMapper;

    
    @Autowired
    @Lazy
    private AccountService accountService;

//    @Override
//    public Page<Profile> getAllProfile(Pageable pageable, String search, ProfileFilterForm form) {
//	Specification<Profile> specification = ProfileSpecification.buildWhere(search, form);
//	return profileRepository.findAll(specification, pageable);
//    }

    @Override
    public Profile getProfileById(String profileId) {
	return profileRepository.findById(profileId)
		.orElseThrow(() -> new EntityNotFoundException("Không tìm thấy profile với ID: " + profileId));
    }

//    @Override
//    public Profile getProfileByPhone(String phone) {
//	return profileRepository.findByPhone(phone).orElse(null);
//    }

    @Override
    @Transactional
    public Profile createProfile(ProfileCreateForm form, Account account) {

	Profile profile = modelMapper.map(form, Profile.class);

	profile = profileRepository.save(profile);
	return profile;
    }

//    @Override
//    public Profile createProfile(ProfileCreateForm form) {
//	Profile profile = modelMapper.map(form, Profile.class);
//	profile = profileRepository.save(profile);
//	return profile;
//    }
//
//    @Override
//    public Profile updateProfile(Profile profile) {
//	return profileRepository.save(profile);
//    }
//
//    @Override
//    public Profile updatePersionalInformationOfProfile(Profile profile,  ProfileUpdateForm form) {
//
//	// Kiểm tra từng trường, nếu không null thì mới cập nhật
//	if (form.getFullname() != null) {
//	    profile.setFullname(form.getFullname());
//	}
//	if (form.getPhone() != null) {
//	    profile.setPhone(form.getPhone());
//	}
//
//	// Lưu lại vào database
//	return profileRepository.save(profile);
//    }
//
//		@Override
//		public Profile updatePersionalInformationOfProfile(String id, ProfileUpdateForm form) {
//				Profile profile = getProfileById(id);
//
//				// Kiểm tra từng trường, nếu không null thì mới cập nhật
//				if (form.getFullname() != null) {
//						profile.setFullname(form.getFullname());
//				}
//				if (form.getPhone() != null) {
//						profile.setPhone(form.getPhone());
//				}
//
//				// Lưu lại vào database
//				return profileRepository.save(profile);
//		}
		
		
}
