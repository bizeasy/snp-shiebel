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

describe('TaxAuthority e2e test', () => {
  const taxAuthorityPageUrl = '/tax-authority';
  let username: string;
  let password: string;
  const taxAuthoritySample = { name: 'attribute' };

  let taxAuthority;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/tax-authorities+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/tax-authorities').as('postEntityRequest');
    cy.intercept('DELETE', '/api/tax-authorities/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (taxAuthority) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/tax-authorities/${taxAuthority.id}`,
      }).then(() => {
        taxAuthority = undefined;
      });
    }
  });

  it('TaxAuthorities menu should load TaxAuthorities page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('tax-authority');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TaxAuthority').should('exist');
    cy.location('pathname').should('eq', taxAuthorityPageUrl);
  });

  describe('TaxAuthority page', () => {
    it('should have translated page title', () => {
      cy.visit(taxAuthorityPageUrl);
      cy.getEntityHeading('TaxAuthority').should('not.contain', 'snpApp.taxAuthority.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(taxAuthorityPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TaxAuthority page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${taxAuthorityPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('TaxAuthority');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', taxAuthorityPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/tax-authorities',
          body: taxAuthoritySample,
        }).then(({ body }) => {
          taxAuthority = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/tax-authorities+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [taxAuthority],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(taxAuthorityPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TaxAuthority page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('taxAuthority');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', taxAuthorityPageUrl);
      });

      it('edit button click should load edit TaxAuthority page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TaxAuthority');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', taxAuthorityPageUrl);
      });

      it('edit button click should load edit TaxAuthority page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TaxAuthority');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', taxAuthorityPageUrl);
      });

      it('last delete button click should delete instance of TaxAuthority', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('taxAuthority').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', taxAuthorityPageUrl);

        taxAuthority = undefined;
      });
    });
  });

  describe('new TaxAuthority page', () => {
    beforeEach(() => {
      cy.visit(taxAuthorityPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('TaxAuthority');
    });

    it('should create an instance of TaxAuthority', () => {
      cy.get(`[data-cy="name"]`).type('ew');
      cy.get(`[data-cy="name"]`).should('have.value', 'ew');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        taxAuthority = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', taxAuthorityPageUrl);
    });
  });
});
