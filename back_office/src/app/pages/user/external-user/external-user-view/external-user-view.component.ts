import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';
import { AppToastNotificationService } from 'src/app/shared/services/appToastNotification.service';
import { TokenUtilsService } from 'src/app/shared/services/token-utils.service';
import { UserService } from '../../../../shared/services/user.service';

@Component({
  selector: 'app-external-user-view',
  templateUrl: './external-user-view.component.html',
  styleUrl: './external-user-view.component.scss'
})
export class ExternalUserViewComponent implements OnInit{

  public user: any;
  public state = window.history.state;
  private userId: string = this.state?.userId;
  private userName: string = this.state?.userName;
  protected userForm: any;
  protected isStationAdmin : boolean;

  constructor(
    private translatePipe: TranslatePipe,
    private router: Router,
    private route: ActivatedRoute,
    private toastrService: AppToastNotificationService,
    private fb: FormBuilder,
    private userService: UserService,
    private tokenUtilsService : TokenUtilsService,
  ) { }


  public ngOnInit(): void {
    this.initFormUser();

    this.userService.getUserById(this.userId).subscribe(
      (res) => {
        this.user = res;
        this.patchFormData();
      },
      (error) => {
        this.toastrService.onError(this.translatePipe.transform('users.loadFailed'), 'User');
      }
    );
  }

  private patchFormData(): void {
    if (!this.user) return;

    this.user.status = this.user.enabled ? 'Actif' : 'Non actif';
    this.user.prefectureId = this.user.prefecture?.name;
    this.user.chefLieuId = this.user.prefecture?.chefLocation?.name;
    this.user.delegationId = this.user.prefecture?.region?.name;
    this.user.groupsId = this.user.role?.group
      ? this.translatePipe.transform('authorities.' + this.user.role?.group.label)
      : '';
    this.user.role = this.user.role?.label
      ? this.translatePipe.transform('authorities.' + this.user.role?.label)
      : '';

    this.user.createdDate = new Date(this.user.createdDate).toLocaleDateString('fr-FR');

    this.userForm.patchValue(this.user);
    this.userForm.disable();
  }

  private initFormUser(): void {
    this.userForm = this.fb.group({
      firstName: [{ value: null, disabled: true }, Validators.required],
      lastName: [{ value: null, disabled: true }, Validators.required],
      // username: [{ value: null, disabled: true }, Validators.required],
      phoneNumber: [{ value: null, disabled: true }, Validators.required],
      email: [{ value: null, disabled: true }, [Validators.required, Validators.email]],
      cin: [{ value: null, disabled: true }, Validators.required],
      address: [{ value: null, disabled: true }, Validators.required]
    });
  }

  protected deleteUser(): void {
    this.userService.deleteUserById(this.userId).subscribe({
        next: () => {
          this.toastrService.onSuccess(this.translatePipe.transform('users.DELETE_SUCCESS'), 'User');
          this.router.navigate(['/pages/user-management/internal-user-management/users']);
        },
        error: () => {
          this.toastrService.onError(this.translatePipe.transform('users.DELETE_FAILED'), 'User');
        }
      }
    )
  }

  protected updateUser() : void {

    const navigationExtras: NavigationExtras = {
      state: {
        userId: this.userId
      }
    };
    this.router.navigate(['pages/user-management/external-user-management/update-user-external', this.userId], navigationExtras);
  }

}
