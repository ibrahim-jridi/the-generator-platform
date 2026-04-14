import { Component } from '@angular/core';
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';
import { Group } from '../../../../shared/models/group.model';
import { AppToastNotificationService } from 'src/app/shared/services/appToastNotification.service';
import { UserService } from '../../../../shared/services/user.service';
import {TokenUtilsService} from "../../../../shared/services/token-utils.service";
import {Role} from "../../../../shared/models/role.model";

@Component({
  selector: 'app-external-user-update',
  templateUrl: './external-user-update.component.html',
  styleUrl: './external-user-update.component.scss'
})
export class ExternalUserUpdateComponent {

  protected userForm: any;
  public state = window.history.state;
  private userId: string = this.state?.userId;
  protected user: any;
  protected groups: Group[];
  protected isAdmin: boolean = true;
  protected showPassword: boolean = false;
  protected showConfirmPassword: boolean = false;
  protected editProfile: boolean = false;
  constructor(
    private translatePipe: TranslatePipe,
    private router: Router,
    private route: ActivatedRoute,
    private toastrService: AppToastNotificationService,
    private fb: FormBuilder,
    private userService: UserService,
    private tokenUtilisService: TokenUtilsService,
  ) { }


  public ngOnInit(): void {
    let username = this.tokenUtilisService.getUsername();
    this.initUserForm();
    this.userService.getUserById(this.userId).subscribe(
      (res) => {
        this.user = res;
        if(this.user && username === this.user.username ) {
          this.editProfile = true
        }
        this.patchFormData();
      },
      (error) => {
        this.toastrService.onError(this.translatePipe.transform('users.loadFailed'), 'User');
      }
    );

  }


  private patchFormData(): void {
    this.userForm.patchValue(this.user);
    this.validateForm();
  }

  private validateForm(): void {
    Object.keys(this.userForm.controls).forEach(field => {
      const control = this.userForm.get(field);
      control.markAsTouched({ onlySelf: true });
    });
  }

  protected onCinInput(event) : void {
    const input = event.target;
    let inputValue = input.value.replace(/\D/g, '').slice(0, 11);
    if (inputValue.length > 4 && inputValue.length <= 7) {
      inputValue = inputValue.slice(0, 4) + '-' + inputValue.slice(4);
    } else if (inputValue.length > 7) {
      inputValue = inputValue.slice(0, 4) + '-' + inputValue.slice(4, 7) + '-' + inputValue.slice(7);
    }
    input.value = inputValue;
    this.userForm.patchValue({ cin: inputValue });
  }


  public onPhoneNumberInput(event: Event): void {
    const inputElement = event.target as HTMLInputElement;
    inputElement.value = inputElement.value.replace(/[^0-9+]/g, '');
    if (inputElement.value.startsWith('+')) {
      inputElement.value = '+' + inputElement.value.substring(1).replace(/\+/g, '');
    }
  }

  protected updateUser(): void {

    this.fillUserData();
    this.userService.updateUser(this.userId,this.user).subscribe({
      next: () => {
        this.viewToasterSuccess();
        this.router.navigate(['pages/user-management-view-user-external', this.userId]);
      },
      error: () => this.viewToasterError()
    });
  }

  private fillUserData(): void {
    this.user.firstName = this.userForm.value.firstName;
    this.user.lastName = this.userForm.value.lastName;
    this.user.email = this.userForm.value.email;

    // this.user.groups = this.groups
    //   .filter(group => this.userForm.value.group.includes(group.id))
    //   .map(group => ({ id: group.id, name: group.label }));

    if (this.userForm.value.password) {
      this.user.password = this.userForm.value.password;
    }
  }

  private initUserForm(): void {
    this.userForm = this.fb.group({
      firstName: [{ value: null }, Validators.required],
      lastName: [{ value: null }, Validators.required],
      username: [{ value: null, disabled: true }, Validators.required],
      phoneNumber: [{ value: null }, Validators.required],
      email: [{ value: null }, [Validators.required, Validators.email]],
      cin: [{ value: null}, Validators.required],
      address: [{ value: null }, Validators.required],
      password: [
        '',
        [ Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%?&])[A-Za-z\d@$!%?&]{8,}$/)]],
      confirmPassword: [
        '',
        [this.confirmPasswordValidator.bind(this), Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%?&])[A-Za-z\d@$!%?&]{8,}$/)]]

    });
  }
  protected confirmPasswordValidator(control: AbstractControl): { [key: string]: any } | null {
    const passwordControl = this.userForm?.get('password');
    const password = passwordControl?.value;
    const confirmPassword = control.value;

    if (!passwordControl?.touched || (!password && !confirmPassword)) {
      return null;
    }

    return password === confirmPassword ? null : { mismatch: true };
  }


  public get password() {
    return this.userForm?.get('password')!;
  }

  public get confirmPassword() {
    return this.userForm?.get('confirmPassword')!;
  }

  public get phone() {
    return this.userForm?.get('phoneNumber')!;
  }

  public get email() {
    return this.userForm?.get('email')!;
  }

  public get cin() {
    return this.userForm?.get('cin')!;
  }

  public get firstName() {
    return this.userForm?.get('firstName')!;
  }

  public get lastName() {
    return this.userForm?.get('lastName')!;
  }

  public viewToasterError(): void {
    this.toastrService.onError(this.translatePipe.transform('users.errors.UPDATE_ERROR'), this.translatePipe.transform('menu.ERROR'));
  }

  public viewToasterSuccess(): void {
    this.toastrService.onSuccess(this.translatePipe.transform('users.UPDATE_SUCCESS'), this.translatePipe.transform('menu.SUCCESS'));
  }

  protected back(): void {
    this.router.navigate(['/pages/user-management/external-user-management']);
  }
  protected toggleConfirmPasswordVisibility(): void {
    this.showConfirmPassword = !this.showConfirmPassword;
  }

  protected togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }
}
