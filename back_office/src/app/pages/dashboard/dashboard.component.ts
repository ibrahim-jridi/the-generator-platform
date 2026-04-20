import { Component, OnInit, OnDestroy } from '@angular/core';
import { TokenUtilsService } from '../../shared/services/token-utils.service';
import { DashboardService } from 'src/app/shared/services/dashboard.service';
import { CamundaService } from '../../shared/services/camunda.service';
import { UserService } from '../../shared/services/user.service';
import { GroupsService } from '../../shared/services/groups.service';
import { AppToastNotificationService } from '../../shared/services/appToastNotification.service';
import { TranslatePipe } from '@ngx-translate/core';
import { Router } from '@angular/router';
import { LoginService } from '../../shared/services/login.service';
import { ApexOptions } from 'apexcharts';
import decode from 'jwt-decode';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit, OnDestroy {
  public username: any;
  public isAdmin: boolean = false;
  public fadeInEffect: boolean = false;
  public chartsReady: boolean = false;

  public stats = {
    processes: { total: 0, active: 0, inactive: 0, trend: 0 },
    tasks: { total: 0, active: 0, completed: 0, trend: 0 },
    users: { total: 0, active: 0, inactive: 0, trend: 0 },
    groups: { total: 0, active: 0, inactive: 0, trend: 0 },
    aiBpmn: { totalGenerated: 0, deployedSuccess: 0, deployedFailed: 0 }
  };

  // ── Process donut ────────────────────────────────────────────────
  processDonutOptions: ApexOptions = {
    chart: { type: 'donut', height: 220, toolbar: { show: false } },
    series: [1, 1],
    labels: ['Active', 'Inactive'],
    colors: ['#4361EE', '#ADB5BD'],
    legend: { position: 'bottom', labels: { colors: ['#6c757d'] } },
    plotOptions: { pie: { donut: { size: '65%', labels: { show: true, total: { show: true, label: 'Total' } } } } },
    dataLabels: { enabled: false },
    stroke: { width: 0 }
  };

  // ── Task bar ─────────────────────────────────────────────────────
  taskBarOptions: ApexOptions = {
    chart: { type: 'bar', height: 220, toolbar: { show: false } },
    series: [
      { name: 'Active', data: [12, 18, 9, 22, 15, 30, 20] },
      { name: 'Completed', data: [30, 42, 38, 55, 48, 60, 45] }
    ],
    colors: ['#4361EE', '#4CC9F0'],
    plotOptions: { bar: { horizontal: false, columnWidth: '45%', borderRadius: 6 } },
    xaxis: {
      categories: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
      labels: { style: { colors: '#9aa3af' } },
      axisBorder: { show: false },
      axisTicks: { show: false }
    },
    yaxis: { labels: { style: { colors: '#9aa3af' } } },
    legend: { position: 'top', labels: { colors: '#6c757d' } },
    dataLabels: { enabled: false },
    grid: { borderColor: '#f1f3f5' },
    stroke: { show: true, width: 2, colors: ['transparent'] }
  };

  // ── User spline ──────────────────────────────────────────────────
  userSplineOptions: ApexOptions = {
    chart: { type: 'area', height: 220, toolbar: { show: false } },
    series: [
      { name: 'Users', data: [40, 52, 61, 70, 78, 85, 90, 102, 110, 118, 130, 145] },
      { name: 'Groups', data: [5, 6, 7, 8, 9, 10, 10, 12, 13, 14, 15, 17] }
    ],
    colors: ['#4361EE', '#F72585'],
    fill: { type: 'gradient', gradient: { shadeIntensity: 1, opacityFrom: 0.3, opacityTo: 0.05 } },
    stroke: { curve: 'smooth', width: 2 },
    xaxis: {
      categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
      labels: { style: { colors: '#9aa3af' } },
      axisBorder: { show: false },
      axisTicks: { show: false }
    },
    yaxis: { labels: { style: { colors: '#9aa3af' } } },
    legend: { position: 'top', labels: { colors: '#6c757d' } },
    dataLabels: { enabled: false },
    grid: { borderColor: '#f1f3f5' }
  };

  // ── AI timeline ──────────────────────────────────────────────────
  aiTimelineOptions: ApexOptions = {
    chart: { type: 'area', height: 220, toolbar: { show: false } },
    series: [
      { name: 'Generated', data: [3, 5, 8, 4, 12, 9, 15, 11, 18, 22, 19, 25] },
      { name: 'Deployed', data: [2, 4, 6, 3, 9, 7, 12, 9, 14, 18, 15, 20] }
    ],
    colors: ['#7B2FBE', '#3A86FF'],
    fill: { type: 'gradient', gradient: { shadeIntensity: 1, opacityFrom: 0.35, opacityTo: 0.05 } },
    stroke: { curve: 'smooth', width: 2 },
    xaxis: {
      categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
      labels: { style: { colors: '#9aa3af' } },
      axisBorder: { show: false },
      axisTicks: { show: false }
    },
    yaxis: { labels: { style: { colors: '#9aa3af' } } },
    legend: { position: 'top', labels: { colors: '#6c757d' } },
    dataLabels: { enabled: false },
    grid: { borderColor: '#f1f3f5' },
    tooltip: { theme: 'light' }
  };

  // ── Model usage donut ────────────────────────────────────────────
  modelUsageOptions: ApexOptions = {
    chart: { type: 'donut', height: 240, toolbar: { show: false } },
    series: [45, 30, 25],
    labels: ['DeepSeek V3.1', 'Qwen3-Coder', 'MiniMax M2.5'],
    colors: ['#7B2FBE', '#3A86FF', '#F72585'],
    legend: { position: 'bottom', labels: { colors: ['#6c757d'] } },
    plotOptions: { pie: { donut: { size: '60%', labels: { show: true, total: { show: true, label: 'Calls' } } } } },
    dataLabels: { enabled: false },
    stroke: { width: 0 }
  };

  // ── Deploy radial ────────────────────────────────────────────────
  deployRadialOptions: ApexOptions = {
    chart: { type: 'radialBar', height: 200, toolbar: { show: false } },
    series: [85],
    plotOptions: {
      radialBar: {
        hollow: { size: '60%' },
        dataLabels: {
          name: { show: true, fontSize: '14px', color: '#6c757d', offsetY: -10 },
          value: { show: true, fontSize: '26px', fontWeight: 700, color: '#212529', formatter: (val) => `${val}%` }
        },
        track: { background: '#e9ecef', strokeWidth: '100%' }
      }
    },
    colors: ['#20C997'],
    labels: ['Success Rate'],
    legend: { show: false },
    stroke: { lineCap: 'round' },
    fill: { type: 'gradient', gradient: { shade: 'dark', type: 'horizontal', gradientToColors: ['#4CC9F0'], stops: [0, 100] } }
  };

  // ── Complexity bar ───────────────────────────────────────────────
  complexityOptions: ApexOptions = {
    chart: { type: 'bar', height: 220, toolbar: { show: false } },
    series: [{ name: 'Generations', data: [38, 45, 17] }],
    colors: ['#7B2FBE', '#3A86FF', '#F72585'],
    plotOptions: { bar: { horizontal: true, borderRadius: 6, distributed: true, barHeight: '50%' } },
    xaxis: {
      categories: ['Simple', 'Medium', 'Complex'],
      labels: { style: { colors: '#9aa3af' } },
      axisBorder: { show: false },
      axisTicks: { show: false }
    },
    yaxis: { labels: { style: { colors: '#6c757d', fontSize: '13px' } } },
    dataLabels: { enabled: true, style: { colors: ['#fff'] } },
    grid: { borderColor: '#f1f3f5', xaxis: { lines: { show: true } }, yaxis: { lines: { show: false } } },
    legend: { show: false }
  };

  constructor(
    private tokenUtilsService: TokenUtilsService,
    private toastrService: AppToastNotificationService,
    private translatePipe: TranslatePipe,
    private router: Router,
    private camundaService: CamundaService,
    private dashboardService: DashboardService,
    private userService: UserService,
    private groupsService: GroupsService,
    private loginService: LoginService
  ) {}

  ngOnInit() {
    this.refreshTokenManually();
    this.username = this.tokenUtilsService.getUsername();

    // ── FIX 1: isAdmin may be a getter or a plain boolean ─────────
    // Try both — never assume it's just a property
    const rawAdmin = (this.tokenUtilsService as any).isAdmin;
    this.isAdmin = typeof rawAdmin === 'function' ? rawAdmin.call(this.tokenUtilsService) : !!rawAdmin;

    this.fadeInEffect = true;
    this.dashboardService.setDashboardState(true);

    // ── FIX 2: always load data, regardless of isAdmin ────────────
    this.loadAllStats();

    // ── FIX 3: delay chart render so ngIf DOM is ready ────────────
    setTimeout(() => {
      this.chartsReady = true;
    }, 100);
  }

  // ── Data loading ──────────────────────────────────────────────────

  private loadAllStats(): void {
    this.loadProcessStats();
    this.loadUserStats();
    this.loadGroupStats();
    this.loadAiBpmnStats();
  }

  private loadProcessStats(): void {
    this.camundaService.getDeployedProcesses().subscribe({
      next: (res: any) => {
        // Handle both plain array and paginated wrapper { content: [...] }
        const list: any[] = Array.isArray(res) ? res : res?.content ?? res?.data ?? [];
        const active = list.filter((p) => !p.suspended).length;
        const inactive = list.length - active;
        this.stats.processes = {
          total: list.length,
          active,
          inactive,
          trend: inactive > 0 ? Math.round(((active - inactive) / inactive) * 100) : 100
        };
        this.processDonutOptions = {
          ...this.processDonutOptions,
          series: [active, inactive]
        };
      },
      error: (e) => console.error('[Dashboard] Process stats error:', e)
    });
  }

  private loadUserStats(): void {
    this.userService
      .getAllUsers()
      .then((res: any) => {
        const list: any[] = Array.isArray(res) ? res : res?.content ?? res?.data ?? [];
        const active = list.filter((u) => u.status === 'active' || u.activated === true).length;
        const inactive = list.length - active;
        this.stats.users = { total: list.length, active, inactive, trend: 8 };
      })
      .catch((e) => console.error('[Dashboard] User stats error:', e));
  }

  private loadGroupStats(): void {
    this.groupsService.fetchAllGroups().subscribe({
      next: (res: any) => {
        const list: any[] = Array.isArray(res) ? res : res?.content ?? res?.data ?? [];
        const active = list.filter((g) => g.status !== 'INACTIVE' && !g.deleted).length;
        const inactive = list.length - active;
        this.stats.groups = { total: list.length, active, inactive, trend: 5 };
      },
      error: (e) => console.error('[Dashboard] Group stats error:', e)
    });
  }

  private loadAiBpmnStats(): void {
    const generated = parseInt(sessionStorage.getItem('aiBpmn_generated') ?? '0', 10);
    const success = parseInt(sessionStorage.getItem('aiBpmn_deployed_success') ?? '0', 10);
    const failed = generated - success;
    this.stats.aiBpmn = { totalGenerated: generated, deployedSuccess: success, deployedFailed: failed };
    const rate = generated > 0 ? Math.round((success / generated) * 100) : 0;
    this.deployRadialOptions = { ...this.deployRadialOptions, series: [rate] };
  }

  // ── Navigation ────────────────────────────────────────────────────

  public onKpiClick(target: string): void {
    const routes: Record<string, string> = {
      process: '/pages/process-management/process-management-bpmn',
      task: '/pages/task-management/task-list',
      users: '/pages/user-management/internal-user-management',
      groups: '/pages/group-management'
    };
    if (routes[target]) {
      this.router.navigate([routes[target]]);
    }
  }

  // ── Token refresh ─────────────────────────────────────────────────

  private refreshTokenManually(): void {
    const refreshToken = this.loginService.getRefreshToken();
    if (!refreshToken) return;
    this.loginService.refreshToken().subscribe({
      next: (newAccessToken: string) => {
        const decoded: any = decode(newAccessToken);
        sessionStorage.setItem('profileCompleted', decoded?.profile_completed);
      },
      error: () => this.loginService.logout()
    });
  }

  ngOnDestroy(): void {
    this.dashboardService.setDashboardState(false);
  }
}