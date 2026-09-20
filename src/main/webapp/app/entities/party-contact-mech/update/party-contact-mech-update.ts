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
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IPartyContactMech } from '../party-contact-mech.model';
import { PartyContactMechService } from '../service/party-contact-mech.service';

import { PartyContactMechFormGroup, PartyContactMechFormService } from './party-contact-mech-form.service';

@Component({
  selector: 'sys-party-contact-mech-update',
  templateUrl: './party-contact-mech-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class PartyContactMechUpdate implements OnInit {
  readonly isSaving = signal(false);
  partyContactMech: IPartyContactMech | null = null;

  partiesSharedCollection = signal<IParty[]>([]);
  contactMechesSharedCollection = signal<IContactMech[]>([]);
  contactMechPurposesSharedCollection = signal<IContactMechPurpose[]>([]);

  protected partyContactMechService = inject(PartyContactMechService);
  protected partyContactMechFormService = inject(PartyContactMechFormService);
  protected partyService = inject(PartyService);
  protected contactMechService = inject(ContactMechService);
  protected contactMechPurposeService = inject(ContactMechPurposeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PartyContactMechFormGroup = this.partyContactMechFormService.createPartyContactMechFormGroup();

  compareParty = (o1: IParty | null, o2: IParty | null): boolean => this.partyService.compareParty(o1, o2);

  compareContactMech = (o1: IContactMech | null, o2: IContactMech | null): boolean => this.contactMechService.compareContactMech(o1, o2);

  compareContactMechPurpose = (o1: IContactMechPurpose | null, o2: IContactMechPurpose | null): boolean =>
    this.contactMechPurposeService.compareContactMechPurpose(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ partyContactMech }) => {
      this.partyContactMech = partyContactMech;
      if (partyContactMech) {
        this.updateForm(partyContactMech);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const partyContactMech = this.partyContactMechFormService.getPartyContactMech(this.editForm);
    if (partyContactMech.id === null) {
      this.subscribeToSaveResponse(this.partyContactMechService.create(partyContactMech));
    } else {
      this.subscribeToSaveResponse(this.partyContactMechService.update(partyContactMech));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IPartyContactMech | null>): void {
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

  protected updateForm(partyContactMech: IPartyContactMech): void {
    this.partyContactMech = partyContactMech;
    this.partyContactMechFormService.resetForm(this.editForm, partyContactMech);

    this.partiesSharedCollection.update(parties =>
      this.partyService.addPartyToCollectionIfMissing<IParty>(parties, partyContactMech.party),
    );
    this.contactMechesSharedCollection.update(contactMeches =>
      this.contactMechService.addContactMechToCollectionIfMissing<IContactMech>(contactMeches, partyContactMech.contactMech),
    );
    this.contactMechPurposesSharedCollection.update(contactMechPurposes =>
      this.contactMechPurposeService.addContactMechPurposeToCollectionIfMissing<IContactMechPurpose>(
        contactMechPurposes,
        partyContactMech.contactMechPurpose,
      ),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(map((parties: IParty[]) => this.partyService.addPartyToCollectionIfMissing<IParty>(parties, this.partyContactMech?.party)))
      .subscribe((parties: IParty[]) => this.partiesSharedCollection.set(parties));

    this.contactMechService
      .query()
      .pipe(map((res: HttpResponse<IContactMech[]>) => res.body ?? []))
      .pipe(
        map((contactMeches: IContactMech[]) =>
          this.contactMechService.addContactMechToCollectionIfMissing<IContactMech>(contactMeches, this.partyContactMech?.contactMech),
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
            this.partyContactMech?.contactMechPurpose,
          ),
        ),
      )
      .subscribe((contactMechPurposes: IContactMechPurpose[]) => this.contactMechPurposesSharedCollection.set(contactMechPurposes));
  }
}
