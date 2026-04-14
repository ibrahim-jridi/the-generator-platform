import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RoleUserListComponent } from './role-user-list.component';

describe('RoleUserListComponent', () => {
  let component: RoleUserListComponent;
  let fixture: ComponentFixture<RoleUserListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RoleUserListComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(RoleUserListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
