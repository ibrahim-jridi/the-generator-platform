import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InternalUserAddComponent } from './internal-user-add.component';

describe('InternalUserAddComponent', () => {
  let component: InternalUserAddComponent;
  let fixture: ComponentFixture<InternalUserAddComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InternalUserAddComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(InternalUserAddComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
