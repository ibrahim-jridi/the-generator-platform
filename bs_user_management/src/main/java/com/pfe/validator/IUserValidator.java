package com.pfe.validator;

import com.pfe.service.dto.ResetPasswordDTO;
import com.pfe.service.dto.UserDTO;

public interface IUserValidator extends IGenericValidator<UserDTO, UserDTO> {

  void checkIfNationalIdExist(String nationalId);

  void checkUserInformations(UserDTO userDTO);
  void beforePasswordChange(ResetPasswordDTO resetPasswordDTO, String token);
}
