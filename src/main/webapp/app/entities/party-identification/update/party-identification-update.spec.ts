import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IPartyIdentificationType } from 'app/entities/party-identification-type/party-identification-type.model';
import { PartyIdentificationTypeService } from 'app/entities/party-identification-type/service/party-identification-type.service';
import { IPartyIdentification } from '../party-identification.model';
import { PartyIdentificationService } from '../service/party-identification.service';

import { PartyIdentificationFormService } from './party-identification-form.service';
import { PartyIdentificationUpdate } from './party-identification-update';

describe('PartyIdentification Management Update Component', () => {
  let comp: PartyIdentificationUpdate;
  let fixture: ComponentFixture<PartyIdentificationUpdate>;
  let activatedRoute: ActivatedRoute;
  let partyIdentificationFormService: PartyIdentificationFormService;
  let partyIdentificationService: PartyIdentificationService;
  let partyService: PartyService;
  let partyIdentificationTypeService: PartyIdentificationTypeService;

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

    fixture = TestBed.createComponent(PartyIdentificationUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    partyIdentificationFormService = TestBed.inject(PartyIdentificationFormService);
    partyIdentificationService = TestBed.inject(PartyIdentificationService);
    partyService = TestBed.inject(PartyService);
    partyIdentificationTypeService = TestBed.inject(PartyIdentificationTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Party query and add missing value', () => {
      const partyIdentification: IPartyIdentification = { id: 10445 };
      const party: IParty = { id: 24527 };
      partyIdentification.party = party;

      const partyCollection: IParty[] = [{ id: 24527 }];
      vi.spyOn(partyService, 'query').mockReturnValue(of(new HttpResponse({ body: partyCollection })));
      const additionalParties = [party];
      const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
      vi.spyOn(partyService, 'addPartyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ partyIdentification });
      comp.ngOnInit();

      expect(partyService.query).toHaveBeenCalled();
      expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(
        partyCollection,
        ...additionalParties.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.partiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call PartyIdentificationType query and add missing value', () => {
      const partyIdentification: IPartyIdentification = { id: 10445 };
      const type: IPartyIdentificationType = { id: 7941 };
      partyIdentification.type = type;

      const partyIdentificationTypeCollection: IPartyIdentificationType[] = [{ id: 7941 }];
      vi.spyOn(partyIdentificationTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: partyIdentificationTypeCollection })));
      const additionalPartyIdentificationTypes = [type];
      const expectedCollection: IPartyIdentificationType[] = [...additionalPartyIdentificationTypes, ...partyIdentificationTypeCollection];
      vi.spyOn(partyIdentificationTypeService, 'addPartyIdentificationTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ partyIdentification });
      comp.ngOnInit();

      expect(partyIdentificationTypeService.query).toHaveBeenCalled();
      expect(partyIdentificationTypeService.addPartyIdentificationTypeToCollectionIfMissing).toHaveBeenCalledWith(
        partyIdentificationTypeCollection,
        ...additionalPartyIdentificationTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.partyIdentificationTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const partyIdentification: IPartyIdentification = { id: 10445 };
      const party: IParty = { id: 24527 };
      partyIdentification.party = party;
      const type: IPartyIdentificationType = { id: 7941 };
      partyIdentification.type = type;

      activatedRoute.data = of({ partyIdentification });
      comp.ngOnInit();

      expect(comp.partiesSharedCollection()).toContainEqual(party);
      expect(comp.partyIdentificationTypesSharedCollection()).toContainEqual(type);
      expect(comp.partyIdentification).toEqual(partyIdentification);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPartyIdentification>();
      const partyIdentification = { id: 24654 };
      vi.spyOn(partyIdentificationFormService, 'getPartyIdentification').mockReturnValue(partyIdentification);
      vi.spyOn(partyIdentificationService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partyIdentification });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(partyIdentification);
      saveSubject.complete();

      // THEN
      expect(partyIdentificationFormService.getPartyIdentification).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(partyIdentificationService.update).toHaveBeenCalledWith(expect.objectContaining(partyIdentification));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPartyIdentification>();
      const partyIdentification = { id: 24654 };
      vi.spyOn(partyIdentificationFormService, 'getPartyIdentification').mockReturnValue({ id: null });
      vi.spyOn(partyIdentificationService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partyIdentification: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(partyIdentification);
      saveSubject.complete();

      // THEN
      expect(partyIdentificationFormService.getPartyIdentification).toHaveBeenCalled();
      expect(partyIdentificationService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IPartyIdentification>();
      const partyIdentification = { id: 24654 };
      vi.spyOn(partyIdentificationService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partyIdentification });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(partyIdentificationService.update).toHaveBeenCalled();
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

    describe('comparePartyIdentificationType', () => {
      it('should forward to partyIdentificationTypeService', () => {
        const entity = { id: 7941 };
        const entity2 = { id: 4837 };
        vi.spyOn(partyIdentificationTypeService, 'comparePartyIdentificationType');
        comp.comparePartyIdentificationType(entity, entity2);
        expect(partyIdentificationTypeService.comparePartyIdentificationType).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
