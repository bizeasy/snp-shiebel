import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('PartyIdentification e2e test', () => {
  const partyIdentificationPageUrl = '/party-identification';
  let username: string;
  let password: string;
  const partyIdentificationSample = { idValue: 'besides huzzah' };

  let partyIdentification;
  let party;
  let partyIdentificationType;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/parties',
      body: {
        isOrganisation: true,
        organisationName: 'so indeed fearless',
        organisationShortName: 'frightfully weakly well',
        salutation: 'hard-to-find yin apu',
        firstName: 'Nash',
        middleName: 'sophisticated',
        lastName: 'Legros',
        gender: 'FEMALE',
        birthDate: '2023-12-14',
        primaryPhone: 'closed',
        primaryEmail: 'unto coolly',
        isTemporaryPassword: false,
        logoImageUrl: 'upwardly straw',
        profileImageUrl: 'usefully',
        notes: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
        postalAddress: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
      },
    }).then(({ body }) => {
      party = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/party-identification-types',
      body: { name: 'after sprinkles', description: 'lest until' },
    }).then(({ body }) => {
      partyIdentificationType = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/party-identifications+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/party-identifications').as('postEntityRequest');
    cy.intercept('DELETE', '/api/party-identifications/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/parties', {
      statusCode: 200,
      body: [party],
    });

    cy.intercept('GET', '/api/party-identification-types', {
      statusCode: 200,
      body: [partyIdentificationType],
    });
  });

  afterEach(() => {
    if (partyIdentification) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/party-identifications/${partyIdentification.id}`,
      }).then(() => {
        partyIdentification = undefined;
      });
    }
  });

  afterEach(() => {
    if (party) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/parties/${party.id}`,
      }).then(() => {
        party = undefined;
      });
    }
    if (partyIdentificationType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/party-identification-types/${partyIdentificationType.id}`,
      }).then(() => {
        partyIdentificationType = undefined;
      });
    }
  });

  it('PartyIdentifications menu should load PartyIdentifications page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('party-identification');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PartyIdentification').should('exist');
    cy.location('pathname').should('eq', partyIdentificationPageUrl);
  });

  describe('PartyIdentification page', () => {
    it('should have translated page title', () => {
      cy.visit(partyIdentificationPageUrl);
      cy.getEntityHeading('PartyIdentification').should('not.contain', 'snpApp.partyIdentification.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(partyIdentificationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PartyIdentification page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${partyIdentificationPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('PartyIdentification');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyIdentificationPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/party-identifications',
          body: {
            ...partyIdentificationSample,
            party,
            type: partyIdentificationType,
          },
        }).then(({ body }) => {
          partyIdentification = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/party-identifications+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/party-identifications?page=0&size=20>; rel="last",<http://localhost/api/party-identifications?page=0&size=20>; rel="first"',
              },
              body: [partyIdentification],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(partyIdentificationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PartyIdentification page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('partyIdentification');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyIdentificationPageUrl);
      });

      it('edit button click should load edit PartyIdentification page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PartyIdentification');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyIdentificationPageUrl);
      });

      it('edit button click should load edit PartyIdentification page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PartyIdentification');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyIdentificationPageUrl);
      });

      it('last delete button click should delete instance of PartyIdentification', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('partyIdentification').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyIdentificationPageUrl);

        partyIdentification = undefined;
      });
    });
  });

  describe('new PartyIdentification page', () => {
    beforeEach(() => {
      cy.visit(partyIdentificationPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PartyIdentification');
    });

    it('should create an instance of PartyIdentification', () => {
      cy.get(`[data-cy="idValue"]`).type('close newsstand lox');
      cy.get(`[data-cy="idValue"]`).should('have.value', 'close newsstand lox');

      cy.get(`[data-cy="party"]`).select(1);
      cy.get(`[data-cy="type"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        partyIdentification = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', partyIdentificationPageUrl);
    });
  });
});
