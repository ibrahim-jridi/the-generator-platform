import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InternalUserUpdateComponent } from './internal-user-update.component';

describe('InternalUserUpdateComponent', () => {
  let component: InternalUserUpdateComponent;
  let fixture: ComponentFixture<InternalUserUpdateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InternalUserUpdateComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(InternalUserUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
