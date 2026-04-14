import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {DemoGuard} from '../shared/guards/demo.guard';
import {ResetPasswordComponent} from '../auth/reset-password/reset-password.component';
import {DashboardGuard} from '../shared/guards/dashboard.guard';
import {UserManagementGuard} from '../shared/guards/user-management.guard';
import {RoleManagementGuard} from '../shared/guards/role-management.guard';
import {GroupManagementGuard} from '../shared/guards/group-management.guard';
import {ProcessManagementGuard} from '../shared/guards/process-management.guard';
import {TransactionManagementGuard} from '../shared/guards/transaction-management.guard';
import {ReportManagementGuard} from '../shared/guards/report-management.guard';
import {TaskManagementGuard} from '../shared/guards/task-management.guard';
import {FormManagementGuard} from '../shared/guards/form-management.guard';
import {ValuesGuard} from '../shared/guards/values.guard';
import {NotificationManagementGuard} from '../shared/guards/notification-management.guard';
import {ConfigurationGedGuard} from '../shared/guards/configuration-ged.guard';
import {GedManagementGuard} from '../shared/guards/ged-management.guard';
import {ConnectorManagementGuard} from '../shared/guards/connector-management.guard';
import {AuditManagementGuard} from '../shared/guards/audit-management.guard';
import {WaitingListManagementGuard} from '../shared/guards/waiting-List-management.guard';
import {PctManagementGuard} from '../shared/guards/pct-management.guard';

const routes: Routes = [
  {
    path: '',
    children: [
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      },
      {
        path: 'forgot-password/:token',
        component: ResetPasswordComponent
      },
      {
        path: 'dashboard',
        canActivateChild: [DashboardGuard],
        loadChildren: () => import('./dashboard/dashboard.module').then((module) => module.DashboardModule)
      },
      {
        path: 'demo',
        canActivateChild: [DemoGuard],
        loadChildren: () => import('./demo/demo.module').then((module) => module.DemoModule)
      },
      {
        path: 'user-management',
        canActivateChild: [UserManagementGuard],
        loadChildren: () => import('./user/user.module').then((module) => module.UserModule)
      },
      {
        path: 'group-management',
        canActivateChild: [GroupManagementGuard],
        loadChildren: () => import('./groups/groups.module').then((module) => module.GroupsModule)
      },
      {
        path: 'process-management',
        canActivateChild: [ProcessManagementGuard],
        loadChildren: () => import('./process-management/process-management.module').then((module) => module.ProcessManagementModule)
      },
      {
        path: 'transaction-management',
        canActivateChild: [TransactionManagementGuard],
        loadChildren: () => import('./transaction/transaction.module').then((module) => module.TransactionModule)
      },
      {
        path: 'role-management',
        canActivateChild: [RoleManagementGuard],
        loadChildren: () => import('./role/role.module').then((module) => module.RoleModule)
      },
      {
        path: 'report-management',
        canActivateChild: [ReportManagementGuard],
        loadChildren: () => import('./rapport/report.module').then((module) => module.RapportModule)
      },
      {
        path: 'task-management',
        canActivateChild: [TaskManagementGuard],
        loadChildren: () => import('./task/task.module').then((module) => module.TaskModule)
      },
      {
        path: 'form-management',
        canActivateChild: [FormManagementGuard],
        loadChildren: () => import('./form-management/form-management.module').then((module) => module.FormManagementModule)
      },
      {
        path: 'list-of-value',
        canActivateChild: [ValuesGuard],
        loadChildren: () => import('./list-of-value/list-of-value.module').then((module) => module.ListOfValueModule)
      },
      {
        path: 'notif-management',
        canActivateChild: [NotificationManagementGuard],
        loadChildren: () => import('./notif-management/notif-management.module').then((module) => module.NotifManagementModule)
      },
      {
        path: 'ged-management',
        canActivateChild: [GedManagementGuard],
        loadChildren: () => import('./ged/ged-file-management/ged-file-management.module').then((m) => m.GedFileManagementModule)
      },
      {
        path: 'configuration-ged',
        canActivateChild: [ConfigurationGedGuard],
        loadChildren: () => import('./configuration-ged/configuration-ged.module').then((m) => m.ConfigurationGedModule)
      },
      {
        path: 'connector-management',
        canActivateChild: [ConnectorManagementGuard],
        loadChildren: () => import('./connector/connector.module').then((module) => module.ConnectorModule)
      },
      {
        path: 'audit-management',
        canActivateChild: [AuditManagementGuard],
        loadChildren: () => import('./audit-management/audit-management.module').then((module) => module.AuditManagementModule)
      },
      {
        path: 'bs-configuration',
        canActivateChild: [AuditManagementGuard],
        loadChildren: () => import('./bs-configuration/bs-configuration.module').then((module) => module.BsConfigurationModule)
      },
      {
        path: 'declaration-interests-management',
        canActivateChild: [TaskManagementGuard],
        loadChildren: () =>
          import('./declaration-interests-management/declaration-interests-management.module').then(
            (module) => module.DeclarationInterestsManagementModule
          )
      },
      {
        path: 'start-of-activity',
        canActivateChild: [TaskManagementGuard],
        loadChildren: () => import('./start-of-activity/start-of-activity.module').then((module) => module.StartOfActivityModule)
      },
      {
        path: 'digital-engagement-management',
        canActivateChild: [TaskManagementGuard],
        loadChildren: () =>
          import('./digital-engagement-management/digital-engagement-management.module').then((module) => module.DigitalEngagementManagementModule)
      },
      {
        path: 'rse-engagement-management',
        canActivateChild: [TaskManagementGuard],
        loadChildren: () =>
          import('./rse-engagement-management/rse-engagement-management.module').then((module) => module.RseEngagementManagementModule)
      },
      {
        path: 'best-practise-management',
        canActivateChild: [TaskManagementGuard],
        loadChildren: () => import('./best-practise-management/best-practise-management.module').then((module) => module.BestPractiseManagementModule)
      },
      {
        path: 'instance-management',
        canActivateChild: [TaskManagementGuard],
        loadChildren: () => import('./process-management/process-management.module').then((module) => module.ProcessManagementModule)
      },
      {
        path: 'ectd',
        loadChildren: () => import('./ectd/ectd.module').then((module) => module.EctdModule)
      },
      {
        path: 'registration-waiting-list',
        canActivateChild: [WaitingListManagementGuard],
        loadChildren: () => import('./registration-waiting-list/registration-waiting-list.module').then((module) => module.RegistrationWaitingListModule)
      },
      { path: 'renewal-region-waiting-list',
        loadChildren: () => import('./renewal-region-waiting-list/renewal-region-waiting-list.module').then(module => module.RenewalRegionWaitingListModule)
      },
      {
        path: 'membre-designe',
        loadChildren: () => import('./membre-designe/membre-designe.module').then((module) => module.MembreDesigneModule)
      },
      {
        path: 'physical-user-profile',
        loadChildren: () => import('./physical-user-profile/physical-user-profile.module').then((module) => module.PhysicalUserProfileModule)
      },
      {
        path: 'unsubscribe-waiting-list',
        loadChildren: () => import('./unsubscribe-waiting-list/unsubscribe-waiting-list.module').then((module) => module.UnsubscribeWaitingListModule)
      },
      {
        path: 'renewal-category-waiting-list',
        loadChildren: () => import('./renewal-category-waiting-list/renewal-category-waiting-list.module').then((module) => module.RenewalCategoryWaitingListModule)
      },
      {
        path: 'agency-pct',
        canActivateChild: [PctManagementGuard],
        loadChildren: () => import('./agency-pct/agency-pct.module').then((module) => module.AgencyPctModule)
      },
      {
        path: 'create-pct',
         canActivateChild: [PctManagementGuard],
        loadChildren: () => import('./create-pct/create-pct.module').then((module) => module.CreatePctModule)
      },
      {
        path: 'subsidiary-pct',
         canActivateChild: [PctManagementGuard],
        loadChildren: () => import('./subsidiary-pct/subsidiary-pct.module').then((module) => module.SubsidiaryPctModule)
      },
      {
        path: 'physical-user-update',
        loadChildren: () => import('./physical-user-update/physical-user-update.module').then((module) => module.PhysicalUserUpdateModule)
      },
      {
        path: 'membre-assignee',
        loadChildren: () => import('./membre-assignee/membre-assignee.module').then((module) => module.MembreAssigneeModule)
      },
      {
        path: 'industrie-humain',
        loadChildren: () => import('./industrie-humain/industrie-humain.module').then((module) => module.IndustrieHumainModule)
      },
      {
        path: 'industrie-vet',
        loadChildren: () => import('./industrie-vet/industrie-vet.module').then((module) => module.IndustrieVetModule)
      },
      {
        path: 'create-grossiste',
        loadChildren: () => import('./create-grossiste/create-grossiste.module').then((module) => module.CreateGrossisteModule)
      },
      {
        path: 'pm-user-profile',
        loadChildren: () => import('./pm-user-profile/pm-user-profile.module').then((module) => module.PmUserProfileModule)
      },
      {
        path: 'pm-user-update-profile',
        loadChildren: () => import('./pm-user-update-profile/pm-user-update-profile.module').then((module) => module.PmUserUpdateProfileModule)
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PagesRoutingModule {}
