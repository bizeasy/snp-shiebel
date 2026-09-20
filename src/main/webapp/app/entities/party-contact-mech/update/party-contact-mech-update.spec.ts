import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IContactMech } from 'app/entities/contact-mech/contact-mech.model';
import { ContactMechService } from 'app/entities/contact-mech/service/contact-mech.service';
import { IContactMechPurpose } from 'app/entities/contact-mech-purpose/contact-mech-purpose.model';
import { ContactMechPurposeService } from 'app/entities/contact-mech-purpose/service/contact-mech-purpose.service';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IPartyContactMech } from '../party-contact-mech.model';
import { PartyContactMechService } from '../service/party-contact-mech.service';

import { PartyContactMechFormService } from './party-contact-mech-form.service';
import { PartyContactMechUpdate } from './party-contact-mech-update';

describe('PartyContactMech Management Update Component', () => {
  let comp: PartyContactMechUpdate;
  let fixture: ComponentFixture<PartyContactMechUpdate>;
  let activatedRoute: ActivatedRoute;
  let partyContactMechFormService: PartyContactMechFormService;
  let partyContactMechService: PartyContactMechService;
  let partyService: PartyService;
  let contactMechService: ContactMechService;
  let contactMechPurposeService: ContactMechPurposeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(PartyContactMechUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    partyContactMechFormService = TestBed.inject(PartyContactMechFormService);
    partyContactMechService = TestBed.inject(PartyContactMechService);
    partyService = TestBed.inject(PartyService);
    contactMechService = TestBed.inject(ContactMechService);
    contactMechPurposeService = TestBed.inject(ContactMechPurposeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Party query and add missing value', () => {
      const partyContactMech: IPartyContactMech = { id: 29906 };
      const party: IParty = { id: 24527 };
      partyContactMech.party = party;

      const partyCollection: IParty[] = [{ id: 24527 }];
      vi.spyOn(partyService, 'query').mockReturnValue(of(new HttpResponse({ body: partyCollection })));
      const additionalParties = [party];
      const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
      vi.spyOn(partyService, 'addPartyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ partyContactMech });
      comp.ngOnInit();

      expect(partyService.query).toHaveBeenCalled();
      expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(
        partyCollection,
        ...additionalParties.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.partiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call ContactMech query and add missing value', () => {
      const partyContactMech: IPartyContactMech = { id: 29906 };
      const contactMech: IContactMech = { id: 5019 };
      partyContactMech.contactMech = contactMech;

      const contactMechCollection: IContactMech[] = [{ id: 5019 }];
      vi.spyOn(contactMechService, 'query').mockReturnValue(of(new HttpResponse({ body: contactMechCollection })));
      const additionalContactMeches = [contactMech];
      const expectedCollection: IContactMech[] = [...additionalContactMeches, ...contactMechCollection];
      vi.spyOn(contactMechService, 'addContactMechToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ partyContactMech });
      comp.ngOnInit();

      expect(contactMechService.query).toHaveBeenCalled();
      expect(contactMechService.addContactMechToCollectionIfMissing).toHaveBeenCalledWith(
        contactMechCollection,
        ...additionalContactMeches.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.contactMechesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call ContactMechPurpose query and add missing value', () => {
      const partyContactMech: IPartyContactMech = { id: 29906 };
      const contactMechPurpose: IContactMechPurpose = { id: 25033 };
      partyContactMech.contactMechPurpose = contactMechPurpose;

      const contactMechPurposeCollection: IContactMechPurpose[] = [{ id: 25033 }];
      vi.spyOn(contactMechPurposeService, 'query').mockReturnValue(of(new HttpResponse({ body: contactMechPurposeCollection })));
      const additionalContactMechPurposes = [contactMechPurpose];
      const expectedCollection: IContactMechPurpose[] = [...additionalContactMechPurposes, ...contactMechPurposeCollection];
      vi.spyOn(contactMechPurposeService, 'addContactMechPurposeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ partyContactMech });
      comp.ngOnInit();

      expect(contactMechPurposeService.query).toHaveBeenCalled();
      expect(contactMechPurposeService.addContactMechPurposeToCollectionIfMissing).toHaveBeenCalledWith(
        contactMechPurposeCollection,
        ...additionalContactMechPurposes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.contactMechPurposesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const partyContactMech: IPartyContactMech = { id: 29906 };
      const party: IParty = { id: 24527 };
      partyContactMech.party = party;
      const contactMech: IContactMech = { id: 5019 };
      partyContactMech.contactMech = contactMech;
      const contactMechPurpose: IContactMechPurpose = { id: 25033 };
      partyContactMech.contactMechPurpose = contactMechPurpose;

      activatedRoute.data = of({ partyContactMech });
      comp.ngOnInit();

      expect(comp.partiesSharedCollection()).toContainEqual(party);
      expect(comp.contactMechesSharedCollection()).toContainEqual(contactMech);
      expect(comp.contactMechPurposesSharedCollection()).toContainEqual(contactMechPurpose);
      expect(comp.partyContactMech).toEqual(partyContactMech);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPartyContactMech>();
      const partyContactMech = { id: 18082 };
      vi.spyOn(partyContactMechFormService, 'getPartyContactMech').mockReturnValue(partyContactMech);
      vi.spyOn(partyContactMechService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partyContactMech });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(partyContactMech);
      saveSubject.complete();

      // THEN
      expect(partyContactMechFormService.getPartyContactMech).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(partyContactMechService.update).toHaveBeenCalledWith(expect.objectContaining(partyContactMech));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPartyContactMech>();
      const partyContactMech = { id: 18082 };
      vi.spyOn(partyContactMechFormService, 'getPartyContactMech').mockReturnValue({ id: null });
      vi.spyOn(partyContactMechService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partyContactMech: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(partyContactMech);
      saveSubject.complete();

      // THEN
      expect(partyContactMechFormService.getPartyContactMech).toHaveBeenCalled();
      expect(partyContactMechService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IPartyContactMech>();
      const partyContactMech = { id: 18082 };
      vi.spyOn(partyContactMechService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partyContactMech });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(partyContactMechService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareParty', () => {
      it('should forward to partyService', () => {
        const entity = { id: 24527 };
        const entity2 = { id: 17981 };
        vi.spyOn(partyService, 'compareParty');
        comp.compareParty(entity, entity2);
        expect(partyService.compareParty).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareContactMech', () => {
      it('should forward to contactMechService', () => {
        const entity = { id: 5019 };
        const entity2 = { id: 29939 };
        vi.spyOn(contactMechService, 'compareContactMech');
        comp.compareContactMech(entity, entity2);
        expect(contactMechService.compareContactMech).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareContactMechPurpose', () => {
      it('should forward to contactMechPurposeService', () => {
        const entity = { id: 25033 };
        const entity2 = { id: 10745 };
        vi.spyOn(contactMechPurposeService, 'compareContactMechPurpose');
        comp.compareContactMechPurpose(entity, entity2);
        expect(contactMechPurposeService.compareContactMechPurpose).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
