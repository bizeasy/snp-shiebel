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

describe('PartyIdentificationType e2e test', () => {
  const partyIdentificationTypePageUrl = '/party-identification-type';
  let username: string;
  let password: string;
  const partyIdentificationTypeSample = { name: 'per' };

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
    cy.intercept('GET', '/api/party-identification-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/party-identification-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/party-identification-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (partyIdentificationType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/party-identification-types/${partyIdentificationType.id}`,
      }).then(() => {
        partyIdentificationType = undefined;
      });
    }
  });

  it('PartyIdentificationTypes menu should load PartyIdentificationTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('party-identification-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PartyIdentificationType').should('exist');
    cy.location('pathname').should('eq', partyIdentificationTypePageUrl);
  });

  describe('PartyIdentificationType page', () => {
    it('should have translated page title', () => {
      cy.visit(partyIdentificationTypePageUrl);
      cy.getEntityHeading('PartyIdentificationType').should('not.contain', 'snpApp.partyIdentificationType.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(partyIdentificationTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PartyIdentificationType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${partyIdentificationTypePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('PartyIdentificationType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyIdentificationTypePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/party-identification-types',
          body: partyIdentificationTypeSample,
        }).then(({ body }) => {
          partyIdentificationType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/party-identification-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [partyIdentificationType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(partyIdentificationTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PartyIdentificationType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('partyIdentificationType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyIdentificationTypePageUrl);
      });

      it('edit button click should load edit PartyIdentificationType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PartyIdentificationType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyIdentificationTypePageUrl);
      });

      it('edit button click should load edit PartyIdentificationType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PartyIdentificationType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyIdentificationTypePageUrl);
      });

      it('last delete button click should delete instance of PartyIdentificationType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('partyIdentificationType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyIdentificationTypePageUrl);

        partyIdentificationType = undefined;
      });
    });
  });

  describe('new PartyIdentificationType page', () => {
    beforeEach(() => {
      cy.visit(partyIdentificationTypePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PartyIdentificationType');
    });

    it('should create an instance of PartyIdentificationType', () => {
      cy.get(`[data-cy="name"]`).type('who');
      cy.get(`[data-cy="name"]`).should('have.value', 'who');

      cy.get(`[data-cy="description"]`).type('ugh whose');
      cy.get(`[data-cy="description"]`).should('have.value', 'ugh whose');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        partyIdentificationType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', partyIdentificationTypePageUrl);
    });
  });
});
