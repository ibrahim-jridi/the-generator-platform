import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewErrorAuditComponent } from './view-error-audit.component';

describe('ViewErrorAuditComponent', () => {
  let component: ViewErrorAuditComponent;
  let fixture: ComponentFixture<ViewErrorAuditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ViewErrorAuditComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ViewErrorAuditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
