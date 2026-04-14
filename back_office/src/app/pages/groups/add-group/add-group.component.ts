import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';
import { AppToastNotificationService } from 'src/app/shared/services/appToastNotification.service';
import { GroupsService } from 'src/app/shared/services/groups.service';
import { Group } from '../../../shared/models/group.model';
import { BsConfigBackEndModel } from '../../../shared/models/bsconfigbackend.model';
import { BsConfigService } from '../../../shared/services/bs-config.service';
import { RegexConstants } from '../../../shared/utils/regex-constants';

@Component({
  selector: 'app-add-group',
  templateUrl: './add-group.component.html',
  styleUrl: './add-group.component.scss'
})
export class AddGroupComponent implements OnInit {
  public groupsForm: FormGroup;
  protected groups: Group[];
  public prefixGroup: string;

  constructor(
    private groupsService: GroupsService,
    private formBuilder: FormBuilder,
    private router: Router,
    private route: ActivatedRoute,
    private translatePipe: TranslatePipe,
    private toastrService: AppToastNotificationService,
    private bsConfigService: BsConfigService
  ) {}

  public ngOnInit(): void {
    this.initGroupsForm();
    this.loadGroups();
    this.getprefixGroup();
  }

  private initGroupsForm(): void {
    this.groupsForm = this.formBuilder.group({
      label: ['', [Validators.required, Validators.pattern(RegexConstants.GROUP_PATTERN_LABEL), Validators.maxLength(16)]],
      description: ['', [Validators.required, Validators.maxLength(150)]],
      dependsOnParentGroup: ['false', Validators.required],
      parent: [null]
    });
  }

  private loadGroups(): void {
    this.groupsService.getGroups().subscribe({
      next: (response: any) => {
        this.groups = response || [];
      },
      error: (error) => {
        this.toastrService.onError(this.translatePipe.transform('groups.FAILED_TO_LOAD_GROUPS'), this.translatePipe.transform('menu.ERROR'));
      }
    });
  }

  public getprefixGroup(): void {
    let config: BsConfigBackEndModel = this.bsConfigService.getConfig();
    this.prefixGroup = config.prefixGroup;
  }

  public addGroup(): void {
    this.groupsForm.value.label = `${this.prefixGroup}${this.groupsForm.get('label')?.value}`;
    this.groupsService.addGroup(this.groupsForm.getRawValue()).subscribe({
      next: (response) => {
        this.toastrService.onSuccess(this.translatePipe.transform('groups.SUCCESS_TO_ADD_GROUP'), this.translatePipe.transform('menu.SUCCESS'));
        this.router.navigate(['../'], { relativeTo: this.route });
      },
      error: (error) => {
        let errors: string[] = error.error.detail.split(',');
        if (errors.length > 0) {
          errors.forEach((error) => {
            this.toastrService.onError(this.translatePipe.transform('groups.' + error), this.translatePipe.transform('menu.ERROR'));
          });
        }
        this.toastrService.onError(this.translatePipe.transform('groups.FAILED_TO_ADD_GROUP'), this.translatePipe.transform('menu.ERROR'));
      }
    });
  }

  protected addOrRemoveParentGroupValidity(hasParent: boolean): void {
    const parentControl = this.groupsForm.get('parent');
    if (hasParent) {
      parentControl.setValidators([Validators.required]);
    } else {
      parentControl.clearValidators();
    }
    parentControl.updateValueAndValidity();
  }

  protected back(): void {
    this.router.navigate(['../'], { relativeTo: this.route });
  }
}
