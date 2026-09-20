import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IRoleType } from '../role-type.model';
import { RoleTypeService } from '../service/role-type.service';

import { RoleTypeFormGroup, RoleTypeFormService } from './role-type-form.service';

@Component({
  selector: 'sys-role-type-update',
  templateUrl: './role-type-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class RoleTypeUpdate implements OnInit {
  readonly isSaving = signal(false);
  roleType: IRoleType | null = null;

  protected roleTypeService = inject(RoleTypeService);
  protected roleTypeFormService = inject(RoleTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: RoleTypeFormGroup = this.roleTypeFormService.createRoleTypeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ roleType }) => {
      this.roleType = roleType;
      if (roleType) {
        this.updateForm(roleType);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const roleType = this.roleTypeFormService.getRoleType(this.editForm);
    if (roleType.id === null) {
      this.subscribeToSaveResponse(this.roleTypeService.create(roleType));
    } else {
      this.subscribeToSaveResponse(this.roleTypeService.update(roleType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IRoleType | null>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving.set(false);
  }

  protected updateForm(roleType: IRoleType): void {
    this.roleType = roleType;
    this.roleTypeFormService.resetForm(this.editForm, roleType);
  }
}
