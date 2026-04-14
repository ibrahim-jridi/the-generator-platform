import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewActivityAuditComponent } from './view-activity-audit.component';

describe('ViewActivityAuditComponent', () => {
  let component: ViewActivityAuditComponent;
  let fixture: ComponentFixture<ViewActivityAuditComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ViewActivityAuditComponent]
    });
    fixture = TestBed.createComponent(ViewActivityAuditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
