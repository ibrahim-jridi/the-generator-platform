import {Component, OnInit} from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {TranslatePipe} from '@ngx-translate/core';
import {Authority} from '../../../shared/models/authority.model';
import {Role} from '../../../shared/models/role.model';
import {AppToastNotificationService} from 'src/app/shared/services/appToastNotification.service';
import {AuthorityService} from '../../../shared/services/authority.service';
import {RoleService} from '../../../shared/services/role.service';
import {BsConfigBackEndModel} from '../../../shared/models/bsconfigbackend.model';
import {BsConfigService} from '../../../shared/services/bs-config.service';

@Component({
  selector: 'app-role-add',
  templateUrl: './role-add.component.html',
  styleUrl: './role-add.component.scss'
})
export class RoleAddComponent implements OnInit {
  protected roleForm: any;
  protected role = new Role();

  protected isUpdateMode: boolean = true;
  protected allSelected: boolean = false;
  protected selectAuthority: boolean = false;
  protected permissionsCount: number = 0;
  protected authorities: Authority[];
  protected authoritiesToSelect: Authority[] = [];
  protected selectedAuthorities: Authority[] = [];
  protected authoritiestoRemove: Authority[] = [];
  public prefixRole: string;

  constructor(
    private translatePipe: TranslatePipe,
    private router: Router,
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private toastrService: AppToastNotificationService,
    private authorityService: AuthorityService,
    private roleService: RoleService,
    private bsConfigService: BsConfigService
  ) {
    this.initRoleForm();
  }

  public ngOnInit(): void {
    this.authorityService.getAllAuthorities().subscribe((authority) => {
      this.authorities = authority;
    });
    this.getprefixRole();
  }

  private initRoleForm(): void {
    this.roleForm = this.fb.group({
      labelle: ['', [Validators.required, Validators.pattern(/^[a-zA-ZÀ-ÿ\s]+$/)]],
      description: ['', [Validators.required, Validators.pattern(/^[a-zA-ZÀ-ÿ\s]+$/)]],
      nbpermission: ['0', [Validators.required]],
      permission: [[''], [Validators.required]],
      selectedAuthoritiesSearchQuery: [''],
      authoritiesSearchQuery: ['']
    });
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

  public get labelle() {
    return this.roleForm?.get('labelle')!;
  }

  public get description() {
    return this.roleForm?.get('description')!;
  }

  public getprefixRole(): void {
    let config: BsConfigBackEndModel = this.bsConfigService.getConfig();
    this.prefixRole = config.prefixRole;
  }

  protected addRole(): void {
    this.role.label = `${this.prefixRole}${this.roleForm.get('labelle')?.value}`;
    this.role.description = this.description.value;
    this.role.authorities = this.selectedAuthorities;
    this.roleService.createRole(this.role).subscribe({
      next: (response) => {
        this.toastrService.onSuccess(this.translatePipe.transform('role.ADD_SUCCESS'), this.translatePipe.transform('menu.SUCCESS'));
        this.back();
      },
      error: (error) => {
        let errors: string[] = error.error.detail.split(',');
        if (errors.length > 0) {
          errors.forEach((error) => {
            this.toastrService.onError(this.translatePipe.transform('role.errors.' + error.trim()), this.translatePipe.transform('menu.ERROR'));
          });
        }
        this.toastrService.onError(this.translatePipe.transform('role.errors.ADD_ERROR'), this.translatePipe.transform('menu.ERROR'));
      }
    });
  }

  protected back(): void {
    this.router.navigate(['../'], { relativeTo: this.route });
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
        this.permissionsCount--;
        this.authorities.push(authority);
      });
      this.authoritiestoRemove = [];
    }
  }
}
