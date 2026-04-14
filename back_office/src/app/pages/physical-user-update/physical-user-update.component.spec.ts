import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PhysicalUserUpdateComponent } from './physical-user-update.component';

describe('PhysicalUserUpdateComponent', () => {
  let component: PhysicalUserUpdateComponent;
  let fixture: ComponentFixture<PhysicalUserUpdateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PhysicalUserUpdateComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PhysicalUserUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
