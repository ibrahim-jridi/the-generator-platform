import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';
import { Authority } from '../../../shared/models/authority.model';
import { Role } from '../../../shared/models/role.model';
import { RoleService } from '../../../shared/services/role.service';
import { AuthorityService } from '../../../shared/services/authority.service';
import { AppToastNotificationService } from '../../../shared/services/appToastNotification.service';

@Component({
  selector: 'app-role-update',
  templateUrl: './role-update.component.html',
  styleUrl: './role-update.component.scss'
})
export class RoleUpdateComponent implements OnInit {
  protected roleForm: any;
  protected role: any;
  public state = window.history.state;
  protected isUpdateMode: boolean = true;
  protected allSelected: boolean = false;
  protected selectAuthority: boolean = false;
  protected permissionsCount: number = 0;
  protected authorities: Authority[];
  protected authoritiesToSelect: Authority[] = [];
  protected selectedAuthorities: Authority[] = [];
  protected authoritiestoRemove: Authority[] = [];
  private roleId: string;
  private roleToUpdate: Role;

  constructor(
    private translatePipe: TranslatePipe,
    private router: Router,
    private toasterService: AppToastNotificationService,
    private fb: FormBuilder,
    private roleService: RoleService,
    private authorityService: AuthorityService
  ) {}

  public ngOnInit(): void {
    this.roleId = this.state.roleId;
    this.authorityService.getAuthoritiesNotInRole(this.roleId).subscribe((authority) => {
      this.authorities = authority;
    });
    this.initRoleForm();
    this.loadRole();
  }

  private loadRole(): void {
    this.roleService.getRoleById(this.roleId).subscribe({
      next: (data) => {
        data.authorities.forEach((authority) => {
          this.selectedAuthorities.push(authority);
        });
        this.roleToUpdate = data;
        this.permissionsCount = this.selectedAuthorities.length;
        this.roleForm.patchValue({
          labelle: data.label,
          description: data.description
        });
      },
      error: (error) => {
        this.toasterService.onError(this.translatePipe.transform('role.errors.FAILED_TO_RETRIEVE_ROLE'), this.translatePipe.transform('menu.ERROR'));
      }
    });
  }

  private initRoleForm(): void {
    this.roleForm = this.fb.group({
      labelle: [
        {
          value: '',
          disabled: true
        },
        [Validators.required, Validators.pattern(/^[a-zA-ZÀ-ÿ\s]+$/)]
      ],
      description: [{ value: '' }, [Validators.required, Validators.pattern(/^[a-zA-ZÀ-ÿ\s_-]+$/)]],
      permission: [[''], [Validators.required]],
      selectedAuthoritiesSearchQuery: [''],
      authoritiesSearchQuery: ['']
    });
    this.roleForm.patchValue({
      permission: [1]
    });
  }

  public get labelle() {
    return this.roleForm?.get('labelle')!;
  }

  public get description() {
    return this.roleForm?.get('description')!;
  }

  protected filteredAuthorities(): Authority[] {
    if (!this.roleForm || !this.roleForm.get('selectedAuthoritiesSearchQuery')) {
      return this.selectedAuthorities;
    }
    const searchQuery = this.roleForm.get('selectedAuthoritiesSearchQuery')?.value ?? '';
    if (searchQuery === '') {
      return this.selectedAuthorities;
    }

    return this.selectedAuthorities.filter((authority) =>
      this.translatePipe.transform(authority.label).toLowerCase().includes(searchQuery.toLowerCase())
    );
  }

  protected filteredAllAuthorities(): Authority[] {
    if (!this.roleForm || !this.roleForm.get('authoritiesSearchQuery')) {
      return this.selectedAuthorities;
    }

    const searchQuery = this.roleForm?.get('authoritiesSearchQuery')?.value ?? '';

    if (searchQuery === '') {
      return this.authorities;
    }
    return this.authorities.filter((authority) => this.translatePipe.transform(authority.label).toLowerCase().includes(searchQuery.toLowerCase()));
  }

  protected updateRole(): void {
    this.roleToUpdate.label = this.labelle.value;
    this.roleToUpdate.description = this.description.value;
    this.roleToUpdate.authorities = this.selectedAuthorities;
    this.roleService.updateRole(this.roleId, this.roleToUpdate).subscribe({
      next: (role) => {
        this.toasterService.onSuccess(this.translatePipe.transform('role.UPDATE_SUCCESS'), this.translatePipe.transform('menu.SUCCESS'));
        this.router.navigate(['/pages/role-management']);
      }, error: (error) => {
        this.toasterService.onError(this.translatePipe.transform('role.errors.UPDATE_ERROR'), this.translatePipe.transform('menu.ERROR'));
      }
    });
  }

  protected back(): void {
    this.router.navigate(['/pages/role-management']);
  }

  protected selectAll(): void {
    this.allSelected = true;
    this.filteredAllAuthorities().forEach((authority) => (authority.checked = true));
    this.authoritiesToSelect = this.filteredAllAuthorities();
  }

  protected unselectAll(): void {
    this.allSelected = false;
    this.filteredAllAuthorities().forEach((authority) => (authority.checked = false));
    this.authoritiesToSelect = [];
  }

  protected removeAll(): void {
    if (this.roleForm?.get('selectedAuthoritiesSearchQuery').value != '') {
      let arr = this.filteredAuthorities();
      arr.forEach((auth) => (auth.checked = false));
      this.authorities = this.authorities.concat(arr);
      this.selectedAuthorities = this.selectedAuthorities.filter((auth) => !arr.includes(auth));
      this.permissionsCount = this.permissionsCount - arr.length;
    } else {
      this.authorities = this.authorities.concat(this.selectedAuthorities);
      this.unselectAll();
      this.selectedAuthorities = [];
      this.permissionsCount = 0;
    }
  }

  protected checkAuthorityToAdd(authority: Authority): void {
    if (authority.checked) {
      authority.checked = false;
      this.authoritiesToSelect = this.authoritiesToSelect.filter((item) => item.id !== authority.id);
    } else {
      authority.checked = true;
      this.authoritiesToSelect.push(authority);
    }
    if (this.authoritiesToSelect.length === this.authorities.length) {
      this.allSelected = true;
    } else {
      this.allSelected = false;
    }
  }

  protected checkAuthorityToRemove(authority: Authority): void {
    if (authority.checked) {
      authority.checked = false;
      this.authoritiestoRemove = this.authoritiestoRemove.filter((item) => item.id !== authority.id);
    } else {
      authority.checked = true;
      this.authoritiestoRemove.push(authority);
    }
  }

  protected addAuthority(id: string): void {
    let authoritySelected = this.authorities.find((item) => item.id === id);
    authoritySelected.checked = false;
    if (this.selectedAuthorities.find((item) => item.id === authoritySelected.id) === undefined) {
      let newAuthority = new Authority(authoritySelected.id, authoritySelected.label, false);
      this.selectedAuthorities.push(newAuthority);
      this.permissionsCount++;
      this.authorities = this.authorities.filter((item) => item.id !== id);
    }
  }

  protected removeAuthority(id: string): void {
    let authoritySelected = this.selectedAuthorities.find((item) => item.id === id);
    authoritySelected.checked = false;
    this.selectedAuthorities = this.selectedAuthorities.filter((authority) => authority.id !== id);
    this.permissionsCount--;
    this.authorities.push(authoritySelected);
  }

  protected addSelectedAuthorities(): void {
    if (this.authoritiesToSelect.length !== 0) {
      this.authoritiesToSelect.forEach((authority) => {
        let newAuthority = new Authority(authority.id, authority.label, false);
        if (this.selectedAuthorities.find((item) => item.id === newAuthority.id) === undefined) {
          this.selectedAuthorities.push(newAuthority);
          this.permissionsCount++;
          this.authorities = this.authorities.filter((authority) => authority.id !== newAuthority.id);
        }
        authority.checked = false;
      });
    }
    this.authoritiesToSelect = [];
    this.allSelected = false;
  }

  protected removeSelectedAuthorities(): void {
    if (this.authoritiestoRemove.length !== 0) {
      this.authoritiestoRemove.forEach((authority) => {
        authority.checked = false;
        this.selectedAuthorities = this.selectedAuthorities.filter((item) => item.id !== authority.id);
        this.authorities.push(authority);
      });
      this.authoritiestoRemove = [];
    }
  }
}
