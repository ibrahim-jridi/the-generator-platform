import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';
import { User } from 'src/app/shared/models/user.model';
import { AppToastNotificationService } from 'src/app/shared/services/appToastNotification.service';
import { TokenUtilsService } from 'src/app/shared/services/token-utils.service';
import { UserService } from '../../../../shared/services/user.service';

@Component({
  selector: 'app-external-user-add',
  templateUrl: './external-user-add.component.html',
  styleUrl: './external-user-add.component.scss'
})
export class ExternalUserAddComponent implements OnInit{

  protected userForm: any;
  protected user = new User();
  protected isAdmin: boolean = true;

  constructor(
    private translatePipe: TranslatePipe,
    private router: Router,
    private route: ActivatedRoute,
    private toastrService: AppToastNotificationService,
    private fb: FormBuilder,
    private userService: UserService,
    private tokenUtilsService : TokenUtilsService,) {
    this.initUserForm();
  }

  public ngOnInit(): void {
  }


  private initUserForm(): void {
    this.userForm = this.fb.group({
      firstName: [
        '',
        [Validators.required, Validators.pattern(/^[a-zA-ZÀ-ÿ\s]+$/)],
      ],
      lastName: [
        '',
        [Validators.required, Validators.pattern(/^[a-zA-ZÀ-ÿ\s]+$/)],
      ],
      phoneNumber: [
        '',
        [
          Validators.required, Validators.pattern(/^\+?[0-9]{8}$/),
        ],
      ],
      email: [
        '',
        [Validators.required, Validators.email, Validators.pattern(/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/)],
      ],
      cin: [
        '',
        [Validators.required, Validators.pattern(/^\d{4}-?\d{3}-?\d{4}$/)]
      ],
      address: [
        '',
        [Validators.required],
      ],
      status: [
        null,
        [Validators.required],
      ]
    });

  }


  public get phoneNumber() {
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

  protected navigateToUsers(): void {
    this.router.navigate(['../'], { relativeTo: this.route });
  }

  protected back(): void {
    this.router.navigate(['../'], { relativeTo: this.route });
  }

  public viewToasterError(): void {
    this.toastrService.onError(this.translatePipe.transform('errors.addError'), this.translatePipe.transform('menu.ERROR'));
  }

  public viewToasterSuccess(): void {
    this.toastrService.onSuccess(this.translatePipe.transform('addSuccess'), this.translatePipe.transform('menu.SUCCESS'));
  }

  private validateForm(): void {
    Object.keys(this.userForm.controls).forEach(field => {
      const control = this.userForm.get(field);
      control.markAsTouched({ onlySelf: true });
    });
  }

  protected addUser(): void {
    this.fillUserData();
    this.userService.saveUser(this.user).subscribe({
      next: () => {
        this.viewToasterSuccess();
        this.router.navigate(['../'], { relativeTo: this.route });
      },
      error: (error) => {
        if (error.error?.detail !== undefined) {
          let errorDetails: string = error.error?.detail;
          if (!errorDetails.includes(',')) {
            this.toastrService.onError(this.translatePipe.transform('errors.' + error.error?.detail), this.translatePipe.transform('menu.ERROR'));
          } else {
            let errors: string[] = error.error?.detail.split(',')
              .map(str => str.trim())
              .map(str => 'errors.' + str)
              .filter(Boolean);
            errors.forEach(error => this.toastrService.onError(this.translatePipe.transform(error), this.translatePipe.transform('menu.ERROR')));
          }
        } else {
          this.toastrService.onError(this.translatePipe.transform('errors.addError'), this.translatePipe.transform('menu.ERROR'));
        }
      },
    });
  }

  protected onCinInput(event) {
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

  private fillUserData(): void {
    this.user.firstName = this.userForm.value.firstName;
    this.user.lastName = this.userForm.value.lastName;
    this.user.phoneNumber = this.userForm.value.phoneNumber;
    this.user.email = this.userForm.value.email;
    this.user.cin = this.userForm.value.cin;
    this.user.address = this.userForm.value.address;
    this.user.setEmailActive = this.userForm.value.status;
    this.user.enabled = this.userForm.value.status;
    this.user.firstConnection = true;
  }

  public onPhoneNumberInput(event: Event): void {
    const inputElement = event.target as HTMLInputElement;

    // Remove any non-numeric characters, except for the plus sign at the start
    inputElement.value = inputElement.value.replace(/[^0-9+]/g, '');

    // Ensure that the plus sign is only at the start
    if (inputElement.value.startsWith('+')) {
      // If there are multiple plus signs, keep only the first one
      inputElement.value = '+' + inputElement.value.substring(1).replace(/\+/g, '');
    }
  }



}
