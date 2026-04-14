import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CostServiceCreationComponent } from './cost-service-creation.component';

describe('ConfigCreationCompanyComponent', () => {
  let component: CostServiceCreationComponent;
  let fixture: ComponentFixture<CostServiceCreationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CostServiceCreationComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CostServiceCreationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
