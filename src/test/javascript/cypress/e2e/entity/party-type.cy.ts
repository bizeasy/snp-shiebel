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

describe('PartyType e2e test', () => {
  const partyTypePageUrl = '/party-type';
  let username: string;
  let password: string;
  const partyTypeSample = { name: 'imaginary very' };

  let partyType;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/party-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/party-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/party-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (partyType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/party-types/${partyType.id}`,
      }).then(() => {
        partyType = undefined;
      });
    }
  });

  it('PartyTypes menu should load PartyTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('party-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PartyType').should('exist');
    cy.location('pathname').should('eq', partyTypePageUrl);
  });

  describe('PartyType page', () => {
    it('should have translated page title', () => {
      cy.visit(partyTypePageUrl);
      cy.getEntityHeading('PartyType').should('not.contain', 'snpApp.partyType.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(partyTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PartyType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${partyTypePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('PartyType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyTypePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/party-types',
          body: partyTypeSample,
        }).then(({ body }) => {
          partyType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/party-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [partyType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(partyTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PartyType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('partyType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyTypePageUrl);
      });

      it('edit button click should load edit PartyType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PartyType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyTypePageUrl);
      });

      it('edit button click should load edit PartyType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PartyType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyTypePageUrl);
      });

      it('last delete button click should delete instance of PartyType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('partyType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyTypePageUrl);

        partyType = undefined;
      });
    });
  });

  describe('new PartyType page', () => {
    beforeEach(() => {
      cy.visit(partyTypePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PartyType');
    });

    it('should create an instance of PartyType', () => {
      cy.get(`[data-cy="name"]`).type('basic plus');
      cy.get(`[data-cy="name"]`).should('have.value', 'basic plus');

      cy.get(`[data-cy="description"]`).type('before');
      cy.get(`[data-cy="description"]`).should('have.value', 'before');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        partyType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', partyTypePageUrl);
    });
  });
});
