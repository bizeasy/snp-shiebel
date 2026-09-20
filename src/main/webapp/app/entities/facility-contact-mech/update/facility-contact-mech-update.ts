import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IContactMech } from 'app/entities/contact-mech/contact-mech.model';
import { ContactMechService } from 'app/entities/contact-mech/service/contact-mech.service';
import { IContactMechPurpose } from 'app/entities/contact-mech-purpose/contact-mech-purpose.model';
import { ContactMechPurposeService } from 'app/entities/contact-mech-purpose/service/contact-mech-purpose.service';
import { IFacility } from 'app/entities/facility/facility.model';
import { FacilityService } from 'app/entities/facility/service/facility.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IFacilityContactMech } from '../facility-contact-mech.model';
import { FacilityContactMechService } from '../service/facility-contact-mech.service';

import { FacilityContactMechFormGroup, FacilityContactMechFormService } from './facility-contact-mech-form.service';

@Component({
  selector: 'sys-facility-contact-mech-update',
  templateUrl: './facility-contact-mech-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class FacilityContactMechUpdate implements OnInit {
  readonly isSaving = signal(false);
  facilityContactMech: IFacilityContactMech | null = null;

  facilitiesSharedCollection = signal<IFacility[]>([]);
  contactMechesSharedCollection = signal<IContactMech[]>([]);
  contactMechPurposesSharedCollection = signal<IContactMechPurpose[]>([]);

  protected facilityContactMechService = inject(FacilityContactMechService);
  protected facilityContactMechFormService = inject(FacilityContactMechFormService);
  protected facilityService = inject(FacilityService);
  protected contactMechService = inject(ContactMechService);
  protected contactMechPurposeService = inject(ContactMechPurposeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FacilityContactMechFormGroup = this.facilityContactMechFormService.createFacilityContactMechFormGroup();

  compareFacility = (o1: IFacility | null, o2: IFacility | null): boolean => this.facilityService.compareFacility(o1, o2);

  compareContactMech = (o1: IContactMech | null, o2: IContactMech | null): boolean => this.contactMechService.compareContactMech(o1, o2);

  compareContactMechPurpose = (o1: IContactMechPurpose | null, o2: IContactMechPurpose | null): boolean =>
    this.contactMechPurposeService.compareContactMechPurpose(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ facilityContactMech }) => {
      this.facilityContactMech = facilityContactMech;
      if (facilityContactMech) {
        this.updateForm(facilityContactMech);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const facilityContactMech = this.facilityContactMechFormService.getFacilityContactMech(this.editForm);
    if (facilityContactMech.id === null) {
      this.subscribeToSaveResponse(this.facilityContactMechService.create(facilityContactMech));
    } else {
      this.subscribeToSaveResponse(this.facilityContactMechService.update(facilityContactMech));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IFacilityContactMech | null>): void {
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

  protected updateForm(facilityContactMech: IFacilityContactMech): void {
    this.facilityContactMech = facilityContactMech;
    this.facilityContactMechFormService.resetForm(this.editForm, facilityContactMech);

    this.facilitiesSharedCollection.update(facilities =>
      this.facilityService.addFacilityToCollectionIfMissing<IFacility>(facilities, facilityContactMech.facility),
    );
    this.contactMechesSharedCollection.update(contactMeches =>
      this.contactMechService.addContactMechToCollectionIfMissing<IContactMech>(contactMeches, facilityContactMech.contactMech),
    );
    this.contactMechPurposesSharedCollection.update(contactMechPurposes =>
      this.contactMechPurposeService.addContactMechPurposeToCollectionIfMissing<IContactMechPurpose>(
        contactMechPurposes,
        facilityContactMech.contactMechPurpose,
      ),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.facilityService
      .query()
      .pipe(map((res: HttpResponse<IFacility[]>) => res.body ?? []))
      .pipe(
        map((facilities: IFacility[]) =>
          this.facilityService.addFacilityToCollectionIfMissing<IFacility>(facilities, this.facilityContactMech?.facility),
        ),
      )
      .subscribe((facilities: IFacility[]) => this.facilitiesSharedCollection.set(facilities));

    this.contactMechService
      .query()
      .pipe(map((res: HttpResponse<IContactMech[]>) => res.body ?? []))
      .pipe(
        map((contactMeches: IContactMech[]) =>
          this.contactMechService.addContactMechToCollectionIfMissing<IContactMech>(contactMeches, this.facilityContactMech?.contactMech),
        ),
      )
      .subscribe((contactMeches: IContactMech[]) => this.contactMechesSharedCollection.set(contactMeches));

    this.contactMechPurposeService
      .query()
      .pipe(map((res: HttpResponse<IContactMechPurpose[]>) => res.body ?? []))
      .pipe(
        map((contactMechPurposes: IContactMechPurpose[]) =>
          this.contactMechPurposeService.addContactMechPurposeToCollectionIfMissing<IContactMechPurpose>(
            contactMechPurposes,
            this.facilityContactMech?.contactMechPurpose,
          ),
        ),
      )
      .subscribe((contactMechPurposes: IContactMechPurpose[]) => this.contactMechPurposesSharedCollection.set(contactMechPurposes));
  }
}
