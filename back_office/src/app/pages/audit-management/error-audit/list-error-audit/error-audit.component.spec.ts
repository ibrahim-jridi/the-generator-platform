import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ErrorAuditComponent } from './error-audit.component';

describe('ErrorAuditComponent', () => {
  let component: ErrorAuditComponent;
  let fixture: ComponentFixture<ErrorAuditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ErrorAuditComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ErrorAuditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
