import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IDepartment } from 'app/entities/department/department.model';
import { DepartmentService } from 'app/entities/department/service/department.service';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IRoleType } from 'app/entities/role-type/role-type.model';
import { RoleTypeService } from 'app/entities/role-type/service/role-type.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IDepartmentMember } from '../department-member.model';
import { DepartmentMemberService } from '../service/department-member.service';

import { DepartmentMemberFormGroup, DepartmentMemberFormService } from './department-member-form.service';

@Component({
  selector: 'sys-department-member-update',
  templateUrl: './department-member-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class DepartmentMemberUpdate implements OnInit {
  readonly isSaving = signal(false);
  departmentMember: IDepartmentMember | null = null;

  departmentsSharedCollection = signal<IDepartment[]>([]);
  partiesSharedCollection = signal<IParty[]>([]);
  roleTypesSharedCollection = signal<IRoleType[]>([]);

  protected departmentMemberService = inject(DepartmentMemberService);
  protected departmentMemberFormService = inject(DepartmentMemberFormService);
  protected departmentService = inject(DepartmentService);
  protected partyService = inject(PartyService);
  protected roleTypeService = inject(RoleTypeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DepartmentMemberFormGroup = this.departmentMemberFormService.createDepartmentMemberFormGroup();

  compareDepartment = (o1: IDepartment | null, o2: IDepartment | null): boolean => this.departmentService.compareDepartment(o1, o2);

  compareParty = (o1: IParty | null, o2: IParty | null): boolean => this.partyService.compareParty(o1, o2);

  compareRoleType = (o1: IRoleType | null, o2: IRoleType | null): boolean => this.roleTypeService.compareRoleType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ departmentMember }) => {
      this.departmentMember = departmentMember;
      if (departmentMember) {
        this.updateForm(departmentMember);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const departmentMember = this.departmentMemberFormService.getDepartmentMember(this.editForm);
    if (departmentMember.id === null) {
      this.subscribeToSaveResponse(this.departmentMemberService.create(departmentMember));
    } else {
      this.subscribeToSaveResponse(this.departmentMemberService.update(departmentMember));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IDepartmentMember | null>): void {
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

  protected updateForm(departmentMember: IDepartmentMember): void {
    this.departmentMember = departmentMember;
    this.departmentMemberFormService.resetForm(this.editForm, departmentMember);

    this.departmentsSharedCollection.update(departments =>
      this.departmentService.addDepartmentToCollectionIfMissing<IDepartment>(departments, departmentMember.department),
    );
    this.partiesSharedCollection.update(parties =>
      this.partyService.addPartyToCollectionIfMissing<IParty>(parties, departmentMember.party),
    );
    this.roleTypesSharedCollection.update(roleTypes =>
      this.roleTypeService.addRoleTypeToCollectionIfMissing<IRoleType>(roleTypes, departmentMember.roleType),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.departmentService
      .query()
      .pipe(map((res: HttpResponse<IDepartment[]>) => res.body ?? []))
      .pipe(
        map((departments: IDepartment[]) =>
          this.departmentService.addDepartmentToCollectionIfMissing<IDepartment>(departments, this.departmentMember?.department),
        ),
      )
      .subscribe((departments: IDepartment[]) => this.departmentsSharedCollection.set(departments));

    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(map((parties: IParty[]) => this.partyService.addPartyToCollectionIfMissing<IParty>(parties, this.departmentMember?.party)))
      .subscribe((parties: IParty[]) => this.partiesSharedCollection.set(parties));

    this.roleTypeService
      .query()
      .pipe(map((res: HttpResponse<IRoleType[]>) => res.body ?? []))
      .pipe(
        map((roleTypes: IRoleType[]) =>
          this.roleTypeService.addRoleTypeToCollectionIfMissing<IRoleType>(roleTypes, this.departmentMember?.roleType),
        ),
      )
      .subscribe((roleTypes: IRoleType[]) => this.roleTypesSharedCollection.set(roleTypes));
  }
}
